package controller;

import contract.IWebService;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.BookingDTO;
import model.CancelAppointmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;
import utils.URL;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = "/cancel")
public class CancelController {

    private final RestClient restClient;
    private final IWebService webService;

    public CancelController(IWebService webService) {
        this.webService = webService;
        this.restClient = RestClient
                .builder()
                .baseUrl(URL.USERS_URL)
                .build();
    }

    @GetMapping
    public String toClinic(
            Authentication authentication,
            Model model
    ) {
        Integer userId = (Integer) authentication.getDetails();
        List<BookingDTO> response = restClient
                .get()
                .uri("/appointments/bookings/patient/" + userId)
                .retrieve()
                .body(List.class);
        model.addAttribute("bookings", response);
        return "cancel";
    }

    @PostMapping
    public String chosenBooking(
            HttpSession session,
            @RequestParam("cancel_booking") Integer bookingId
    ) {
        if (bookingId != null) {
            session.setAttribute("cancel_booking", bookingId);
            return "redirect:/cancel/confirm";
        } else
            return "redirect:/cancel?error=booking_not_chosen";
    }

    @GetMapping("/confirm")
    public String showClinic(HttpSession session) {
        Object bookingObj = session.getAttribute("cancel_booking");
        Integer bookingId = (bookingObj instanceof Integer n) ? n : null;
        if (bookingId != null) {
            return "confirm_cancellation";
        } else
            return "redirect:/cancel?error=booking_not_chosen";
    }

    @PostMapping("/confirm/submit")
    public String cancel(HttpSession session) throws ServiceException {
        Object bookingObj = session.getAttribute("cancel_booking");
        Integer bookingId = (bookingObj instanceof Integer n) ? n : null;
        if (bookingId != null) {
            CancelAppointmentDTO cancelAppointment = new CancelAppointmentDTO();
            cancelAppointment.setAppointmentId(bookingId);
            webService.cancelAppointment(cancelAppointment);
            session.removeAttribute("cancel_booking");
            return "redirect:/menu";
        } else
            return "redirect:/cancel?error=booking_not_chosen";
    }

    @PostMapping("/confirm/rescind")
    public String rescind(HttpSession session){
        Object bookingObj = session.getAttribute("cancel_booking");
        Integer bookingId = (bookingObj instanceof Integer n) ? n : null;
        if(bookingId != null){
            session.removeAttribute("cancel_booking");
            return "success_cancellation";
        } else
            return "redirect:/cancel?error=booking_not_chosen";
    }
    
    @GetMapping("/confirm/success")
    public String success(){
        return "success_cancellation";
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Error")
    public String handleError() {
        return "redirect:/schedule/menu?error=unknown_error";
    }
}