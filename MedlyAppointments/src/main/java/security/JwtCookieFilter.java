package security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Will store the token
        String token = null;
        // Looks out for the token among the user's cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        //If the token is present
        if (token != null && !token.isEmpty()) {
            try {
                // Decodes the token
                DecodedJWT decodedJWT = JWTUtil.validateToken(token);
                // Decodes the token's expected payload
                String role = decodedJWT.getClaim("role").asString();
                String email = decodedJWT.getClaim("email").asString();
                Integer userId = decodedJWT.getClaim("id").asInt();
                // Checks whether the user's email and role are present and if such role equals 'PATIENT'
                if (email != null && role != null && userId != null && role.equalsIgnoreCase("PATIENT") && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Formats the role value to follow Spring Boot's semantics
                    String springRole = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role.toUpperCase();
                    // Stores the user's role as an authority and the email as the authorizarion principal
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(springRole);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));
                    // Stores the user's id in the authorization's details
                    auth.setDetails(userId);
                    // Creates a new security context with all the previous generated and retrieved information
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext(); // If no token is present, security context is cleared
            }
        }
        filterChain.doFilter(request, response);
    }
}