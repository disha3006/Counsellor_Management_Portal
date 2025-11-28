package disha.ManagementSystem.controller;


import disha.ManagementSystem.ExcelExporter;
import disha.ManagementSystem.entity.Enquiry;
import disha.ManagementSystem.entity.User;

import disha.ManagementSystem.repo.CommentsRepo;
import disha.ManagementSystem.repo.EnquiryRepo;
import disha.ManagementSystem.repo.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String dashboard(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        if (userId == null) return "redirect:/login";

        User loggedUser = userRepo.findById(userId).orElse(null);

        Pageable pageable = PageRequest.of(page, size);
        Page<Enquiry> enquiriesPage;

        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);


        if (isAdmin) {
            enquiriesPage = enquiryRepo.findAll(pageable);
        } else {
            enquiriesPage = enquiryRepo.findByCounsellor(loggedUser, pageable);
        }

        model.addAttribute("enquiries", enquiriesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", enquiriesPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        model.addAttribute("enquiryForm", new Enquiry());
        model.addAttribute("counsellorList", userRepo.findByRole("COUNSELLOR"));


        long inProgress;
        long enrolled;
        long lost;
        long newCount;

        if (isAdmin) {
            inProgress = enquiryRepo.countByStatus("In Progress");
            enrolled = enquiryRepo.countByStatus("Enrolled");
            lost = enquiryRepo.countByStatus("Lost");
            newCount = enquiryRepo.countByStatus("New");
        } else {
            inProgress = enquiryRepo.countByCounsellorAndStatus(loggedUser, "In Progress");
            enrolled = enquiryRepo.countByCounsellorAndStatus(loggedUser, "Enrolled");
            lost = enquiryRepo.countByCounsellorAndStatus(loggedUser, "Lost");
            newCount = enquiryRepo.countByCounsellorAndStatus(loggedUser, "New");
        }


        long activeEnq;
        long urgentEnq;
        long completedEnq = msgRepo.count();
        long totalEnq;

        if (isAdmin) {
            activeEnq = enquiryRepo.count();
            urgentEnq = enquiryRepo.countByUrgency("High");
        } else {
            activeEnq = enquiryRepo.countByCounsellor(loggedUser);
            urgentEnq = enquiryRepo.countByCounsellorAndUrgency(loggedUser, "High");
        }

        totalEnq = activeEnq + completedEnq;


        model.addAttribute("inProgressCount", inProgress);
        model.addAttribute("enrolledCount", enrolled);
        model.addAttribute("lostCount", lost);
        model.addAttribute("newCount", newCount);

        model.addAttribute("activeEnq", activeEnq);
        model.addAttribute("urgent", urgentEnq);
        model.addAttribute("completedEnq", completedEnq);
        model.addAttribute("totalEnq", totalEnq);

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

        // Counsellor auto-assign enquiry to themselves
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


    @GetMapping("/enquiry/export/excel")
    public void exportToExcel(HttpServletResponse response) throws Exception {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=enquiries.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Enquiry> listEnquiries = enquiryRepo.findAll();

        ExcelExporter exporter = new ExcelExporter(listEnquiries);
        exporter.export(response);
    }



}



