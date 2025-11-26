package disha.ManagementSystem.controller;


import disha.ManagementSystem.entity.Enquiry;
import disha.ManagementSystem.entity.User;

import disha.ManagementSystem.repo.CommentsRepo;
import disha.ManagementSystem.repo.EnquiryRepo;
import disha.ManagementSystem.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class DashboardController {

    @Autowired
    private EnquiryRepo enquiryRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentsRepo msgRepo;


    @GetMapping("/Dashboard")
    public String dashboard(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        User loggedUser = userRepo.findById(userId).orElse(null);

        List<Enquiry> enquiries;

        // Counsellor sees only their own enquiries
        if ("COUNSELLOR".equalsIgnoreCase(role)) {
            enquiries = enquiryRepo.findByCounsellor(loggedUser);
        }
        // Admin + student will see all enquiries
        else {
            enquiries = enquiryRepo.findAll();
        }

        model.addAttribute("enquiries", enquiries);

        model.addAttribute("enquiryForm", new Enquiry());

        model.addAttribute("counsellorList", userRepo.findByRole("COUNSELLOR"));




        //foor charts
        long inProgress = enquiryRepo.countByStatus("In Progress");
        long enrolled = enquiryRepo.countByStatus("Enrolled");
        long lost = enquiryRepo.countByStatus("Lost");
        long newCourse = enquiryRepo.countByStatus("New");



        long activeEnquirires = enquiryRepo.count();
        long urgentEnquries = enquiryRepo.countByUrgency("High");
        long completedEnquries = msgRepo.count();
        long totalEnquries = activeEnquirires + completedEnquries;

        model.addAttribute("inProgressCount", inProgress);
        model.addAttribute("enrolledCount", enrolled);
        model.addAttribute("lostCount", lost);
        model.addAttribute("newCount", newCourse);

        model.addAttribute("activeEnq",activeEnquirires );
        model.addAttribute("urgent",urgentEnquries );
        model.addAttribute("completedEnq", completedEnquries);
        model.addAttribute("totalEnq", totalEnquries);
        return "Dashboard";
    }




    @PostMapping("/enquiry/save")
    public String saveEnquiry(@ModelAttribute("enquiryForm") Enquiry enq,
                              @RequestParam(value = "counsellorId", required = false) Long counsellorId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        User loggedUser = userRepo.findById(userId).orElse(null);

        // COUNSELLOR → auto-assign enquiry to themselves
        if ("COUNSELLOR".equalsIgnoreCase(role)) {
            enq.setCounsellor(loggedUser);
        }

        else {
            User selected = userRepo.findById(counsellorId).orElse(null);
            enq.setCounsellor(selected);
        }

        enquiryRepo.save(enq);

        redirectAttributes.addFlashAttribute("successMsg", "Enquiry added successfully!");
        return "redirect:/Dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }




    @GetMapping("/profile")
    public String myprofile(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        if (userId == null) return "redirect:/login";

        User user = userRepo.findById(userId).orElse(null);
        model.addAttribute("user", user);

        model.addAttribute("userForm", user);

        return "MyProfile";
    }

    @PostMapping("/profile/update")
    public String profileUpdate(@ModelAttribute("userForm") User userForm,
                                RedirectAttributes ra) {

        userRepo.save(userForm);

        ra.addFlashAttribute("successMsg", "Profile updated successfully!");

        return "redirect:/profile";
    }




}



