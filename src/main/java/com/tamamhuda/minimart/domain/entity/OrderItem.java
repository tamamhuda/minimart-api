package com.tamamhuda.minimart.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class OrderItem {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private Float price;

}
