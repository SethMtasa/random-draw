package com.netone.random_draw.security;

import com.netone.random_draw.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CurrentAuditor implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                if (authentication.getPrincipal() instanceof User user) {
                    return Optional.of(user);
                }
            }
        } catch (Exception e) {
            log.error("Authentication:{}", e.getMessage());
        }
        return Optional.empty();
    }


    public User getLoggedInUserOrThrow() throws Exception {
        return getCurrentAuditor().orElseThrow(() ->  new Exception("Invalid login user"));
    }

    public String getUsernameOrThrow() throws Exception {
        return getLoggedInUserOrThrow().getUsername();
    }
}