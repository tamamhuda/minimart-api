package com.tamamhuda.minimart.domain.repository;

import com.tamamhuda.minimart.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
