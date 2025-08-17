package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.dto.UserRequestChangePassword;
import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.mapper.UserRequestMapper;
import com.tamamhuda.minimart.application.service.UserService;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialException;
import java.time.Instant;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRequestMapper userRequestMapper;
    private final S3ServiceImpl s3Service;

    public User getUserByUsername(String username) throws ResponseStatusException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."));

    }

    public User getUserByEmail(String email) throws ResponseStatusException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is not registered."));

    }

    public User getByUsernameOrEmail(String username) throws UnauthorizedException {
        try {

            return username.contains("@")
                    ? getUserByEmail(username)
                    : getUserByUsername(username);

        } catch (ResponseStatusException e) {
            throw new UnauthorizedException(e.getReason());
        }
    }

    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken.");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public UserDetails validateCredentials(String username, String password) throws UnauthorizedException {
        UserDetails user = getByUsernameOrEmail(username);
        boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());

        if (!isPasswordValid) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        return user;
    }

    public User findUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.")
        );
    }


    @Override
    @Cacheable(cacheNames = "users", key = "#user.id")
    public UserDto me(User user) throws UnauthorizedException {
        return userMapper.toDto(user);
    }

    private User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    private User updateProfileImage(User user, String profileImage) {
        user.setProfileImage(profileImage);
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    @Override
    @CachePut(cacheNames = "users", key = "#userId")
    public UserDto uploadProfileImage(MultipartFile file, UUID userId) {
        User existingUser = findUserById(userId);

        String imageUrl = s3Service.uploadImage(file, "users");
        User updatedUser = updateProfileImage(existingUser, imageUrl);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public PageDto<UserDto> getAllUsers(Pageable pageable) {
        Page<UserDto> page = userRepository.findAll(pageable).map(userMapper::toDto);

        return PageDto.<UserDto>builder()
                .content(page.getContent())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .last(page.isLast())
                .build();
    }

    @Override
    @CachePut(cacheNames = "users", key = "#userId")
    public UserDto changePassword(UserRequestChangePassword request, UUID userId) throws CredentialException {
        User existingUser = findUserById(userId);
        String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());

        if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password must match");
        }
        User updatedUser = updatePassword(existingUser, hashedNewPassword);

        return userMapper.toDto(updatedUser);
    }

    @Override
    @CachePut(cacheNames = "users", key = "#userId")
    public UserDto update(UserRequestDto request, UUID userId) {
        User existingUser = findUserById(userId);
        userRequestMapper.updateFromRequestDto(request, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @CacheEvict(cacheNames = "users", key = "#user.id")
    public void verifyUser(User user) {
        user.setVerified(true);
        user.setEnabled(true);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Override
    public void proxyProfileImage(HttpServletResponse response, String imageUrl) {
        s3Service.proxyImage(response, "users", imageUrl);
    }


}
