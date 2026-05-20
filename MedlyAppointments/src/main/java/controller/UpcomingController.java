package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Leonardo Flores Leyva
 */
@Controller
@RequestMapping(path = "/upcoming")
public class UpcomingController {
    
   @GetMapping
   public String showUpcoming(){
       
       return "upcoming";
   }
}