package controller;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping("/new")
public class AppointmentController {
    
    @GetMapping
    public void redirect(HttpSession session){
        showDoctor(session);
    }
    
    /**
     * Retrieves the consulting room/doctor selection page.
     * @param session Current user's session
     * @return The document's name
     */
    @GetMapping("/doctor")
    public String showDoctor(HttpSession session) {
        // Retrieves the necessary attribute from the session
        Object userObj = session.getAttribute("user");
        // Casts the object to the desired type (null otherwise)
        Integer userId = (userObj instanceof Integer id) ? id : null;
        // Retrieves the page if the user's id is present
        return (userId != null) ? "doctor" : "redirect:/index";
    }
    
    /**
     * Catches the data from the consulting room/doctor page's form.
     * Redirects the user to the datetime selection page.
     * Step 2 of the appointment registration process.
     * @param session
     * @param consultingRoomId
     * @return 
     */
    @PostMapping("/doctor")
    public String formDoctor(
            HttpSession session,
            @RequestParam("consulting_room") Integer consultingRoomId
    ){
        if(consultingRoomId != null){
            session.setAttribute("consulting_room", consultingRoomId);
            return "redirect:/datetime";
        } else 
            return "redirect:/doctor";
    }
    /**
     * 
     * @param session
     * @return 
     */
    @GetMapping("/datetime")
    public String showDateTime(HttpSession session){
        // Retrieves the necessary attributes from the session
        Object userObj = session.getAttribute("user");
        Object cRoomObj = session.getAttribute("consulting_room");
        // Casts the objects to the desired types (null otherwise)
        Integer userId = (userObj instanceof Integer id) ? id : null;
        Integer consultingRoomId = (cRoomObj instanceof Integer n) ? n : null;
        // Redirects the user to the datetime selection page if both ids are present
        return (userId != null && consultingRoomId != null) ? "datetime" : "redirect:/doctor";
    }
    
    @PostMapping("/register")
    public String registerForm(
            HttpSession session,
            @RequestParam("datetime") LocalDateTime dateTime
    ){
        if(dateTime != null){
            // All attributes must be present by this point
            Integer userId = (Integer) session.getAttribute("user");
            Integer consultingRoomId = (Integer) session.getAttribute("consulting_room");
            // Tries to register the new appointment in the appointment service...
            return "redirect:/register";
        } else
            return "redirect:/menu";
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Dumbass")
    public void handleError() {}
}