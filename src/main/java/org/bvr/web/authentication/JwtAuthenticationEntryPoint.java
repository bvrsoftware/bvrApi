package org.bvr.web.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException authException) throws IOException, ServletException {

        String fMsg = httpServletResponse.getHeader("F-msg");
        if(StringUtils.isNotEmpty(fMsg)) {
            httpServletResponse.sendError(HttpServletResponse.SC_OK,fMsg);
        }else {
            String xMsg = httpServletResponse.getHeader("X-msg");
            String xStatusCode=httpServletResponse.getHeader("X-status-code");

            if(StringUtils.isNotEmpty(xMsg)) {
                if(StringUtils.isBlank(xStatusCode)){
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,xMsg);
                }else{
                    httpServletResponse.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),xMsg);
                }
            }else {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
            }
        }

    }
}
