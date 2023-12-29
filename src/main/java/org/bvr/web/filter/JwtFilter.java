package org.bvr.web.filter;

import com.bvr.core.domain.BvrSoftwareUser;
import com.bvr.core.domain.service.BvrSoftwareUserService;
import com.bvr.core.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static List<String> BLOCKED_USER_LIST = Collections.emptyList();
    private String BVRLITE_PROFILE = "bvrlite";
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BvrSoftwareUserService bvrSoftwareUserService;
    @Autowired
    private Environment environment;


    private String getJwtFromRequest(HttpServletRequest request,HttpServletResponse response,String path) {
        String jwt = null;
        final String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7).trim();
        } else {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null) {
                jwt = Stream.of(cookies).filter(ck -> ck.getName().equals("Token")).findFirst().orElse(new Cookie("Token", null)).getValue();
            }
        }
        return jwt;
    }

    private boolean doNotSkipRequest(String path) {
        return(path.contains("/rest")||path.contains("/ajax")||path.contains("/web"));
    }

    private boolean skipRequest(String path) {
        return StringUtils.equals(path, "/bvr/") || StringUtils.containsAny(path, "/resources","/login","/v3/signin","/public","/privacyPolicy","/share");
    }

    private String getPath(HttpServletRequest request) {
        String path = "";
        try {
            path = request.getRequestURI();
        } catch (Exception e) {}
        return path;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = getPath(request);
        if(skipRequest(path)) {
            log.debug("[ "+path+" ]Skipping................");
        }else {
            final String jwt = getJwtFromRequest(request,response, path);
            //String jwt = request.getHeader("Authorization").substring(7).trim();

            if (StringUtils.isNotEmpty(jwt)) {
                try {
                    Claims claims = JwtTokenProvider.extractAllClaims(jwt);
                    final String username = JwtTokenProvider.extractUsername(claims);
                    if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                        final BvrSoftwareUser bvrSoftwareUser = bvrSoftwareUserService.findByUsername(username);
                        log.debug("Bvr software User - " + bvrSoftwareUser);
                        if (bvrSoftwareUser == null ) {
                            response.setHeader("X-msg","User does not exists - " + username);
                        }else if (!bvrSoftwareUser.isEnabled()) {
                            response.setHeader("X-msg","User account deactivated, Please reach admin to enable user");
                        }else if (bvrSoftwareUser.isAccountSuspended()) {
                            response.setHeader("X-msg","Dear customer, your account has been suspended due to non payment of your service provider");
                        }else if (!StringUtils.equals(jwt, bvrSoftwareUser.getJwt())) {
                            //response.setHeader("X-msg","This account has been logged-in on another system.");
                            //response.setHeader("X-status-code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
                            response.setHeader("X-msg","User session expired - " + username);
                        } else {
                            List<GrantedAuthority> authorities = (List<GrantedAuthority>) bvrSoftwareUser.getAuthorities();
                            if(BVRLITE_PROFILE.equalsIgnoreCase(environment.getActiveProfiles()[0])){
                                authorities.add(new SimpleGrantedAuthority("ROLE_BVRLITE_USER"));
                            }
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(bvrSoftwareUser, null, authorities);
                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    } else {
                        response.setHeader("X-msg","Invalid token - " + jwt);
                    }
                } catch (Exception ex) {
                    response.setHeader("X-msg",ex.getMessage());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
