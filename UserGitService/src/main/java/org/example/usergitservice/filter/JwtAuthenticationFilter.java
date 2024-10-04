package org.example.usergitservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.example.usergitservice.config.SecurityConfig;
import org.example.usergitservice.services.JwtService;
import org.example.usergitservice.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        logger.info("Processing request for URI : {}", request.getRequestURI());
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            logger.warn("No Authorization header or does not start with 'Bearer ' ");
            filterChain.doFilter(request,response);
            return;
        }
        String token =authHeader.substring(7);
        logger.debug("Extracted token: {}", token);
        String idNumber =jwtService.extractId(token);
        logger.info("Extracted id : {}",idNumber);
        if(idNumber!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            logger.info("ID number is valid and no existing authentication found");

            UserDetails userDetails =myUserDetailsService.loadUserByUsername(idNumber);
            logger.debug("Loaded user details for ID: {}", idNumber);

            if(jwtService.isValid(token,userDetails)){
                logger.info("Token is valid, setting authentication");

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                logger.warn("Token is not valid for user ID: {}", idNumber);
            }
        }
        logger.info("Proceeding with filter chain");
        filterChain.doFilter(request,response);

    }
}
