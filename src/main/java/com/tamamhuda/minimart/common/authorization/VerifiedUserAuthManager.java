package com.tamamhuda.minimart.common.authorization;

import com.tamamhuda.minimart.domain.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class VerifiedUserAuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        User user = (User) auth.getPrincipal();

        if (!user.isVerified()) throw new AccessDeniedException("Access denied. User is not verified");

        return new AuthorizationDecision(true);
    }
}
