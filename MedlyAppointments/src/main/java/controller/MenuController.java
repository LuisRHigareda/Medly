package controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = {"/menu", "/home"})
public class MenuController {
    
    @GetMapping
    public String showMenu(HttpSession session){
        Integer userId = (Integer) session.getAttribute("user");
        return (userId == null) ? "redirect:/index" : "home";
    }
}