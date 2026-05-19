package controller;

import client.LoginRequest;
import client.LoginResponse;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import security.JWTUtil;

/**
 * Handles the home page's petitions
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = {"/index", "/"})
public class HomeController {

    private final RestClient restClient;

    public HomeController() {
        this.restClient = RestClient
                .builder()
                .baseUrl("http://localhost:8080/api")
                .build();
    }

    /**
     * Retrieves the home page. If the user is logged in, he is redirected to
     * the home page.
     *
     * @param principal
     * @return The home page document's name
     */
    @GetMapping
    public String showHome(Principal principal) {
        return (principal == null) ? "index" : "redirect:/menu";
    }

    @PostMapping("/login")
    public String login(
            HttpServletResponse response,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        try {
            // Sends credentials and receives the generated token
            LoginResponse authResponse = restClient
                    .post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(email, password))
                    .retrieve()
                    .body(LoginResponse.class);
            // Retrieves the token
            String jwtToken = authResponse.getJwtToken();
            // Decodes the token and retrieves the user's role
            DecodedJWT decoded = JWTUtil.validateToken(jwtToken);
            String role = decoded.getClaim("role").asString();
            // Checks if the authenticated user is a patient
            if (role == null || !role.equalsIgnoreCase("PATIENT")) {
                return "redirect:/index?error=unauthorized_role";
            }
            // Stores the token in a cookie
            Cookie jwtCookie = new Cookie("jwt-token", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            // Add the cookie to the response (the cookie it's stored in the user's browser)
            response.addCookie(jwtCookie);
            // Redirects the patient to the menu page
            return "redirect:/menu";

        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            return "redirect:/index?error=bad_credentials";
        } catch (Exception e) {
            return "redirect:/index?error=unknown_error";
        }
    }
}