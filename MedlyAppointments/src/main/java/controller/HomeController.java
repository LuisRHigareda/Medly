package controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles the home page's petitions
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = {"/index", "/"})
public class HomeController {
    /**
     * Retrieves the home page. If the user is logged in,
     * he is redirected to the home page.
     * @param session User's session
     * @return The home page document's name
     */
    @GetMapping
    public String showHome(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user");
        return (userId == null) ? "index" : "redirect:/menu";
    }
    
    @PostMapping("/login")
    public String showMenu(
            HttpSession session,
            @RequestParam("email") String email,
            @RequestParam("affiliation_number") String affiliationNumber,
            @RequestParam("password") String password
    ){
        if((email != null || affiliationNumber != null) && password != null){
            // This would be where the user's id is retrieved to later store it in the session...
            // Hardcoded for now...
            Integer userId = 1;
            session.setAttribute("user", userId);
            return "redirect:/menu";
        } else
            return "redirect:/index?error=params_missing";
    }
}