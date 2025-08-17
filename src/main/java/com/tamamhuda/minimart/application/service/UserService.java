package com.tamamhuda.minimart.application.service;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.dto.UserRequestChangePassword;
import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.common.dto.PageDto;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialException;
import java.util.UUID;

public interface UserService {

    User getUserByUsername(String username) throws ResponseStatusException;

    User getUserByEmail(String email) throws UnauthorizedException;

    User getByUsernameOrEmail(String username) throws ResponseStatusException;

    User createUser(User user);

    UserDetails validateCredentials(String username, String password) throws UnauthorizedException;

    void verifyUser(User user);

    UserDto me(User user);

    UserDto update(UserRequestDto request, UUID userId);

    UserDto changePassword(UserRequestChangePassword request, UUID userId) throws CredentialException;

    PageDto<UserDto> getAllUsers(Pageable pageable);

    void deleteUserById(UUID userId);

    UserDto uploadProfileImage(MultipartFile file, UUID userId);

    void proxyProfileImage(HttpServletResponse response, String imageUrl) ;
}
