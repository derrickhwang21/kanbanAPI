package com.hwang.kanban.security;

import com.hwang.kanban.domain.User;
import com.hwang.kanban.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.hwang.kanban.security.SecurityConstants.HEADER_STRING;
import static com.hwang.kanban.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try{
            String jwt = getJWTFromRequest(httpServletRequest);

            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
                Long userId = tokenProvider.getUseridFromJWT(jwt);
                User userDetails = customUserDetailsService.loadUserByid(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }catch (Exception ex){
            logger.error("Could not set user authentication in security context", ex);

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
