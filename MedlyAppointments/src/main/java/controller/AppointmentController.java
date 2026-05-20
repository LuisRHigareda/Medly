package controller;

import client.ClinicResponse;
import client.ConsultingRoomResponse;
import contract.IWebService;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.NewAppointmentDTO;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import utils.URL;

/**
 * Handles the appointment scheduling process
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping("/schedule")
public class AppointmentController {

    private final IWebService webService;
    private final RestClient restClient;

    public AppointmentController(IWebService webService) {
        this.webService = webService;
        this.restClient = RestClient
                .builder()
                .baseUrl(URL.USERS_URL)
                .build();
    }

    @GetMapping
    public String redirect() {
        return "redirect:/schedule/clinic";
    }

    @GetMapping("/clinic")
    public String showClinic(Model model) {
        List<ClinicResponse> response = restClient
                .get()
                .uri("/users/clinics")
                .retrieve()
                .body(List.class);
        model.addAttribute("clinics", response);
        return "clinic";
    }

    @PostMapping("/clinic")
    public String formClinic(
            HttpSession session,
            @RequestParam("clinic") Integer clinicId
    ) {
        if (clinicId != null) {
            session.setAttribute("clinic", clinicId);
            return "redirect:/schedule/doctor";
        } else {
            return "redirect:/schedule/clinic?error=clinic_missing";
        }
    }

    /**
     * Retrieves the consulting room/doctor selection page.
     *
     * @param session
     * @param model
     * @return The document's name
     */
    @GetMapping("/doctor")
    public String showDoctor(HttpSession session, Model model) {
        Object clinicObj = session.getAttribute("clinic");
        Integer clinicId = (clinicObj instanceof Integer n) ? n : null;
        if (clinicId != null) {
            List<ConsultingRoomResponse> response = restClient
                    .get()
                    .uri("/users/consulting_room/clinic/" + clinicId)
                    .retrieve()
                    .body(List.class);
            model.addAttribute("doctors", response);
            return "doctor";
        } else {
            return "redirect:/schedule/clinic?error=doctor_missing";
        }
    }

    /**
     * Catches the data from the consulting room/doctor page's form. Redirects
     * the user to the datetime selection page.
     *
     * @param session
     * @param consultingRoomId
     * @return
     */
    @PostMapping("/doctor")
    public String formDoctor(
            HttpSession session,
            @RequestParam("consulting_room") Integer consultingRoomId
    ) {
        if (consultingRoomId != null) {
            session.setAttribute("consulting_room", consultingRoomId);
            return "redirect:/schedule/datetime";
        } else {
            return "redirect:/schedule/doctor?error=doctor_missing";
        }
    }

    /**
     * Retrieves the datetime selection page.
     *
     * @param session
     * @return
     */
    @GetMapping("/datetime")
    public String showDateTime(HttpSession session) {
        Object cRoomObj = session.getAttribute("consulting_room");
        Integer consultingRoomId = (cRoomObj instanceof Integer n) ? n : null;
        return (consultingRoomId != null) ? "datetime" : "redirect:/schedule/doctor?error=doctor_missing";
    }

    @PostMapping("/submit")
    public String registerForm(
            HttpSession session,
            @RequestParam("datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            Authentication authentication
    ) throws ServiceException {
        // Retrieves the consulting room's id and checks if it's present
        Integer consultingRoomId = (Integer) session.getAttribute("consulting_room");
        if (consultingRoomId == null) {
            return "redirect:/schedule/doctor?error=doctor_missing";
        }

        // Checks whether the user submited an existing date and time
        if (dateTime != null) {

            // Finally, retrieves the user's id
            Integer userId = (Integer) authentication.getDetails();
            // Builds up the transferable object
            NewAppointmentDTO appointment = new NewAppointmentDTO();
            appointment.setPatientId(userId);
            appointment.setConsultingRoomId(consultingRoomId);
            appointment.setDateTime(dateTime);
            // Executes the web services method
            webService.newAppointment(appointment);
            // Clears the consulting room and clinic's id from the session
            session.removeAttribute("consulting_room");
            session.removeAttribute("clinic");

            return "redirect:/schedule/success";
        } else {
            return "redirect:/schedule/datetime?error=datetime_missing";
        }
    }

    @GetMapping("/success")
    public String successScheduling(HttpSession session) {
        return "success_schedule";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Error")
    public String handleError() {
        return "redirect:/schedule/menu?error=unknown_error";
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
    public String handleNotFound() {
        return "redirect:/schedule/menu?error=not_found";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect information")
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return "redirect:/schedule/menu?error=invalid_data";
    }
}
