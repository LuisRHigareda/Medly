package controller;

import client.LoginRequest;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import security.JWTUtil;
import utils.URL;

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
                .baseUrl(URL.USERS_URL)
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
    ) throws UnsupportedEncodingException {

        // Sends credentials and receives the generated token
        Map<String, String> authResponse = restClient
                .post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest(email, password))
                .retrieve().body(Map.class);
        // Retrieves the token
        String jwtToken = authResponse.get("jwt-token");
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
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Error")
    public String handleError() {
        return "redirect:/index?error=unknown_error";
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public String handleNotFound() {
        return "redirect:/schedule/index?error=bad_credentials";
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden")
    public String handleForbidden() {
        return "redirect:/schedule/index?error=bad_credentials";
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unsupported Encoding")
    public String handleUnssoportedEncoding() {
        return "redirect:/index?error=internal_error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Illegal Argument")
    public String handleIllegalArgument() {
        return "redirect:/index?error=internal_error";
    }
}