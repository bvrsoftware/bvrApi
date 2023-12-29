package org.bvr.web.authentication;

import com.bvr.core.domain.BvrSoftwareUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Log4j2
@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        BvrSoftwareUser bvrSoftwareUser = (BvrSoftwareUser) userDetailsService.loadUserByUsername(username);
        String encryptedPassword = bvrSoftwareUser.getPassword();
        if (!passwordEncoder.matches(password, encryptedPassword)) {
            throw new BadCredentialsException("Please enter the correct password !!");
        }
        Collection<? extends GrantedAuthority> authorities = bvrSoftwareUser.getAuthorities();
        return new UsernamePasswordAuthenticationToken(bvrSoftwareUser, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
