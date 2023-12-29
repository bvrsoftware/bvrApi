package org.bvr.web.authentication;

import com.bvr.core.domain.BvrSoftwareUser;
import com.bvr.core.domain.service.BvrSoftwareUserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Log4j2
@Component("inMemoryAuthenticationProvider")
public class InMemoryAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BvrSoftwareUserService bvrSoftwareUserService;

    @PostConstruct
    private void init() {

    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        BvrSoftwareUser bvrSoftwareUser=(BvrSoftwareUser) bvrSoftwareUserService.loadUserByUsername(username);
        if (StringUtils.equals(username, bvrSoftwareUser.getUsername()) && passwordEncoder.matches(password,bvrSoftwareUser.getPassword())) {
            Collection<? extends GrantedAuthority> grantedAuthorities = bvrSoftwareUser.getAuthorities();
            return new UsernamePasswordAuthenticationToken(bvrSoftwareUser, password, grantedAuthorities);
        } else {
            throw new BadCredentialsException("Please enter the correct password !!");
        }    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);    }
}
