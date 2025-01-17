package com.netone.random_draw.security;

import com.netone.random_draw.model.User;
import com.netone.random_draw.repository.UserRepository;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import java.util.Collection;

public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {
    private final UserRepository userRepository;

    public CustomUserDetailsContextMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found."));
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
