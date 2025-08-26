package com.tamamhuda.minimart.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.Bucket4jJCache.JCacheProxyManagerBuilder;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.api.NameMapper;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.cache.CacheManager;
import javax.cache.Caching;
import java.time.Duration;
import java.util.regex.Pattern;

@Configuration
@EnableCaching
@Profile("!test")
public class RedisConfig {

    @Value("${spring.cache.redis.key-prefix}")
    private String cacheKeyPrefix;

    @Value("${bucket4j.key-prefix}")
    private String bucketKeyPrefix;

    @Value("${bucket4j.redis-address}")
    private String redisClient;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        ObjectMapper mapper = objectMapper.copy();
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(serializer))
                .computePrefixWith(cacheName -> cacheKeyPrefix + ":" + cacheName + ":");

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }


    @Bean
    public Config config() {
        String escapedPrefix = Pattern.quote(bucketKeyPrefix + ":");
        Config config = new Config();
        config.useSingleServer().setAddress(redisClient)
                .setNameMapper(new NameMapper() {
                    @Override
                    public String map(String name) {
                        return bucketKeyPrefix + ":" + name;
                    }
                    @Override
                    public String unmap(String name) {
                        return name.replaceFirst("^" + escapedPrefix, "");
                    }
                });
        return config;
    }

    @Bean
    public CacheManager buckectCacheManager(Config config) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("auth", RedissonConfiguration.fromConfig(config));
        manager.createCache("order", RedissonConfiguration.fromConfig(config));
        return manager;
    }


    @Bean
    public ProxyManager<String> authProxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(new JCacheProxyManagerBuilder<>(cacheManager.getCache("auth")));
    }

    @Bean
    public ProxyManager<String> orderProxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(new JCacheProxyManagerBuilder<>(cacheManager.getCache("order")));
    }
}
