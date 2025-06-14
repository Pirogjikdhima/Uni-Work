package inxh.softi.webprojekt.detyrekursi.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final HttpSession session;

    public PageController(HttpSession session) {
        this.session = session;
    }

    @GetMapping("/register")
    public String register() {
        String role = (String) session.getAttribute("role");
        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin";
        } else if ("ROLE_USER".equals(role)) {
            return "redirect:/user";
        }
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        String role = (String) session.getAttribute("role");
        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin";
        } else if ("ROLE_USER".equals(role)) {
            return "redirect:/user";
        }
        return "login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));

        String role = (String) session.getAttribute("role");
        if (!"ROLE_ADMIN".equals(role)) {
            if ("ROLE_USER".equals(role)) {
                return "redirect:/user";
            } else {
                return "redirect:/login";
            }
        }
        return "admin";
    }

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));

        String role = (String) session.getAttribute("role");
        if (!"ROLE_USER".equals(role)) {
            if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/admin";
            } else {
                return "redirect:/login";
            }
        }
        return "user";
    }
}