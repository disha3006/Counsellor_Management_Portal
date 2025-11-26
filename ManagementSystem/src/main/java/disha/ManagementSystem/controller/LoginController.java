package disha.ManagementSystem.controller;

import disha.ManagementSystem.entity.User;
import disha.ManagementSystem.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class LoginController {


    @Autowired
    private UserRepo userRepo;


    @GetMapping("/register")
    public String register_user(@ModelAttribute("userForm") User user){
        return "Register";
    }

    @PostMapping("/register")
    public String add_user(@ModelAttribute("userForm") User user){
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register";
        }
     //   user.setRole("COUNSELLOR");  //this role will be default for the registration. if u want admin role to be set, use db.
        userRepo.save(user);
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String login_user(){
        return "Login";
    }

    @PostMapping("/login")
    public String enter_login(@ModelAttribute("userForm") User formUser, HttpSession session){
        Optional<User> optUser = userRepo.findByEmail(formUser.getEmail());

        if (optUser.isPresent() && optUser.get().getPassword().equals(formUser.getPassword())) {

            User u = optUser.get();

            session.setAttribute("loggedUserId", u.getId());
            session.setAttribute("loggedUserName", u.getName());
            session.setAttribute("loggedUserRole", u.getRole());

            return "redirect:/Dashboard";
        }
        return "redirect:/Login";
    }
}
