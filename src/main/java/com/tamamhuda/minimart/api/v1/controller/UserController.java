package com.tamamhuda.minimart.api.v1.controller;

import com.tamamhuda.minimart.application.dto.UserDto;
import com.tamamhuda.minimart.application.dto.UserRequestChangePassword;
import com.tamamhuda.minimart.application.dto.UserRequestDto;
import com.tamamhuda.minimart.application.mapper.UserMapper;
import com.tamamhuda.minimart.application.service.impl.UserServiceImpl;
import com.tamamhuda.minimart.common.annotation.CurrentUser;
import com.tamamhuda.minimart.common.annotation.RequiredRoles;
import com.tamamhuda.minimart.common.dto.PageResponse;
import com.tamamhuda.minimart.common.validation.group.Update;
import com.tamamhuda.minimart.domain.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.CredentialException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@CurrentUser User user) {
        UserDto response = userService.me(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<UserDto> update(@Validated(Update.class) @RequestBody UserRequestDto request, @PathVariable("user_id") UUID userId) {
        UserDto response = userService.update(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{user_id}/change-password")
    public ResponseEntity<UserDto> changePassword(@PathVariable("user_id") UUID userId, @Valid @RequestBody UserRequestChangePassword request) throws CredentialException {
        UserDto response = userService.changePassword(request, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") UUID userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toDto(user));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(Pageable pageable) {
        PageResponse<UserDto> user = userService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiredRoles({"ADMIN"})
    public ResponseEntity<?> deleteUserById(@PathVariable("user_id") UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{user_id}/upload")
    public ResponseEntity<UserDto> uploadProfileImage(@PathVariable("user_id") UUID userId, @RequestParam("file") MultipartFile file){
        UserDto response = userService.uploadProfileImage(file, userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/images/{imageUrl}")
    public void proxyProductImage(@PathVariable("imageUrl") String imageUrl, HttpServletResponse response){
        userService.proxyProfileImage(response, imageUrl);
    }


}
