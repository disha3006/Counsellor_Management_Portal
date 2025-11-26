package disha.ManagementSystem.controller;

import disha.ManagementSystem.entity.Comment;
import disha.ManagementSystem.entity.Enquiry;
import disha.ManagementSystem.entity.User;
import disha.ManagementSystem.repo.CommentsRepo;
import disha.ManagementSystem.repo.EnquiryRepo;
import disha.ManagementSystem.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private EnquiryRepo enquiryRepo;

    @Autowired
    private CommentsRepo commentRepo;

    @Autowired
    private UserRepo userRepo;


    @GetMapping("/enquiry/resolvePage")
    public String openResolvePage(@RequestParam("id") Long id, Model model) {
        Enquiry enq = enquiryRepo.findById(id).orElse(null);
        model.addAttribute("id", id);
        return "ResolveEnquiry";
    }


    @GetMapping("/resolvedEnquiries")
    public String viewResolvedEnquiries(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        String role = (String) session.getAttribute("loggedUserRole");

        User loggedUser = userRepo.findById(userId).orElse(null);
        List<Comment> list;

        if ("ADMIN".equalsIgnoreCase(role)) {
            list = commentRepo.findAll();
        }
        else {
            list = commentRepo.findByCounsellor(loggedUser);
        }
        model.addAttribute("resolvedList", list);
        return "CompletedEnquiries";
    }



    @PostMapping("/enquiry/resolve")
    public String resolveEnquiry(@RequestParam("id") Long id,
                                 @RequestParam("message") String message,
                                 HttpSession session) {

        Enquiry enq = enquiryRepo.findById(id).orElse(null);
        Long loggedUserId = (Long) session.getAttribute("loggedUserId");
        User counsellor = userRepo.findById(loggedUserId).orElse(null);

        Comment c = new Comment();
        c.setEnquiryId(id);
        c.setMessage(message);
        c.setStudentName(enq.getStudent_name());
        c.setCounsellor(counsellor);
        commentRepo.save(c);
        enquiryRepo.deleteById(id);
        return "redirect:/resolvedEnquiries";
    }

}
