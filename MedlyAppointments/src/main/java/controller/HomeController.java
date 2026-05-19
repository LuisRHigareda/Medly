package controller;

import client.LoginRequest;
import client.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Handles the home page's petitions
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = {"/index", "/"})
public class HomeController {
    
    private final RestClient restClient;

    public HomeController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8080/api")
                .build();
    }
    
    /**
     * Retrieves the home page. If the user is logged in,
     * he is redirected to the home page.
     * @param jwtToken
     * @return The home page document's name
     */
    @GetMapping
    public String showHome(@CookieValue(value = "jwt-token", required = false) String jwtToken) {
        return (jwtToken == null || jwtToken.isEmpty()) ? "index" : "redirect:/menu";
    }
    
    @PostMapping("/login")
    public String login(
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        try {
            LoginResponse authResponse = restClient
                    .post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(email, password))
                    .retrieve()
                    .body(LoginResponse.class);
            
            String jwtToken = authResponse.getJwtToken();
            
            Cookie jwtCookie = new Cookie("jwt-token", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 24);
            
            response.addCookie(jwtCookie);
            
            return "redirect:/menu";
            
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            return "redirect:/index?error=bad_credentials";
        } catch (Exception e) {
            return "redirect:/index?error=unknown_error";
        }
    }
}