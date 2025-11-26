package disha.ManagementSystem.controller;

import disha.ManagementSystem.entity.User;
import disha.ManagementSystem.repo.EnquiryRepo;
import disha.ManagementSystem.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ManageCounsellorController {

    @Autowired
    private EnquiryRepo enquiryRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/manageCounsellors")
    public String manageCounsellors(Model m){
        m.addAttribute("counsellors", userRepo.findAll());
        m.addAttribute("counsellorForm", new User());
        return "CounsellorMgmt";
    }


    @PostMapping("/counsellor/delete")
    public String deleteCounsellor(@RequestParam Long id,
                                   RedirectAttributes ra) {
        userRepo.deleteById(id);
        ra.addFlashAttribute("successMsg", "Counsellor deleted successfully!");
        return "redirect:/manageCounsellors";
    }


    @PostMapping("/counsellor/save")
    public String saveCounsellor(@ModelAttribute("counsellorForm") User user,
                                 RedirectAttributes ra) {

        user.setRole("COUNSELLOR"); // auto assign role
        userRepo.save(user);

        ra.addFlashAttribute("successMsg", "Counsellor added successfully!");
        return "redirect:/manageCounsellors";
    }
}
