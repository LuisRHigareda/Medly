package controller;

import java.util.List;
import model.BookingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import utils.URL;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = "/upcoming")
public class UpcomingController {

    private final RestClient restClient;

    public UpcomingController() {
        this.restClient = RestClient
                .builder()
                .baseUrl(URL.USERS_URL)
                .build();
    }

    @GetMapping
    public String showUpcoming(
            Authentication authentication,
            Model model
    ) {
        Integer userId = (Integer) authentication.getDetails();
        List<BookingDTO> response = restClient
                .get()
                .uri((t) -> t
                .path("/appointments/bookings/patient/status")
                .queryParam("id", userId)
                .queryParam("status", "TO_BE_CONFIRMED").build())
                .retrieve()
                .body(List.class);
        model.addAttribute("bookings", response);
        return "upcoming";
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Error")
    public String handleError() {
        return "redirect:/menu?error=unknown_error";
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
    public String handleBadRequest() {
        return "redirect:/menu?error=internal-error";
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public String handleNotFound() {
        return "redirect:/menu?error=not_found";
    }
}