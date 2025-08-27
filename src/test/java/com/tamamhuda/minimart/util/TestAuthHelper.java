package com.tamamhuda.minimart.testutil;

import com.tamamhuda.minimart.common.authorization.VerifiedUserAuthManager;
import com.tamamhuda.minimart.application.service.impl.JwtServiceImpl;
import com.tamamhuda.minimart.application.service.impl.UserDetailsServiceImpl;
import com.tamamhuda.minimart.domain.enums.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@Component
public class TestAuthHelper {

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private VerifiedUserAuthManager verifiedUserAuthManager;

    /** Mock authenticated user using only Role */
    public UserDetails mockAuthenticatedUser(Role role) {
        Mockito.when(jwtService.extractUsername(anyString())).thenReturn("testuser");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .roles(role.name())
                .build();

        Mockito.when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        Mockito.when(jwtService.isAccessTokenValid("dummy-access-token", userDetails)).thenReturn(true);
        Mockito.when(verifiedUserAuthManager.check(any(Supplier.class), any()))
                .thenReturn(new AuthorizationDecision(true));

        return userDetails;
    }

    /** Authorization header for MockMvc */
    public RequestPostProcessor authorizationToken(UserDetails userDetails) {
        return request -> {
            request.addHeader("Authorization", "Bearer dummy-access-token");
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return request;
        };
    }
}
