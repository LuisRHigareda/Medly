package controller;

import contract.IWebService;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import model.NewAppointmentDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Handles the appointment scheduling process
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping("/schedule")
public class AppointmentController {
    
    private final IWebService webService;
    
    public AppointmentController(IWebService webService){
        this.webService = webService;
    }
    
    @GetMapping
    public String redirect(){return "redirect:/schedule/doctor";}
    
    /**
     * Retrieves the consulting room/doctor selection page.
     * @return The document's name
     */
    @GetMapping("/doctor") 
    public String showDoctor() {return "doctor";}
    
    /**
     * Catches the data from the consulting room/doctor page's form.
     * Redirects the user to the datetime selection page.
     * @param session
     * @param consultingRoomId
     * @return 
     */
    @PostMapping("/doctor_submit") 
    public String formDoctor(
            HttpSession session,
            @RequestParam("consulting_room") Integer consultingRoomId
    ){
        if(consultingRoomId != null){
            session.setAttribute("consulting_room", consultingRoomId);
            return "redirect:/schedule/datetime";
        } else 
            return "redirect:/schedule/doctor?error=doctor_missing";
    }

    /**
     * Retrieves the datetime selection page.
     * @param session
     * @return 
     */
    @GetMapping("/datetime") 
    public String showDateTime(HttpSession session){
        Object cRoomObj = session.getAttribute("consulting_room");
        Integer consultingRoomId = (cRoomObj instanceof Integer n) ? n : null;
        return (consultingRoomId != null) ? "datetime" : "redirect:/schedule/doctor?error=doctor_missing";
    }
    
    @PostMapping("/submit") 
    public String registerForm(
            HttpSession session,
            @RequestParam("datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            Authentication authentication
    ){        
        // Retrieves the consulting room's id and checks if it's present
        Integer consultingRoomId = (Integer) session.getAttribute("consulting_room");
        if(consultingRoomId == null) return "redirect:/schedule/doctor?error=doctor_missing";
        
        // Checks whether the user submited an existing date and time
        if(dateTime != null){
            try {
                // Finally, retrieves the user's id
                Integer userId = (Integer) authentication.getDetails();
                // Builds up the transferable object
                NewAppointmentDTO appointment = new NewAppointmentDTO();
                appointment.setPatientId(userId);
                appointment.setConsultingRoomId(consultingRoomId);
                appointment.setDateTime(dateTime);
                // Executes the web services method
                webService.newAppointment(appointment);
                // Clears the consulting room's id from the session
                session.removeAttribute("consulting_room");
                
            } catch (ServiceException e) {
                return "redirect:/schedule/datetime?error=unknown_error"; 
            }
            return "redirect:/schedule/success";
        } else
            return "redirect:/schedule/datetime?error=datetime_missing";
    }
    
    @GetMapping("/success") 
    public String successScheduling(HttpSession session){return "success_schedule";}
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unknown Error")
    public String handleError() {return "redirect:/schedule/menu?error=unknown_error";}
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect information")
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return "redirect:/schedule/menu?error=invalid_data";
    }
}