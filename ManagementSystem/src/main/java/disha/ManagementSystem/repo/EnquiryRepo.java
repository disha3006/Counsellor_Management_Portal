package disha.ManagementSystem.repo;

import disha.ManagementSystem.entity.Enquiry;
import disha.ManagementSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepo extends JpaRepository<Enquiry, Long> {

    // Pagination
    Page<Enquiry> findByCounsellor(User counsellor, Pageable pageable);

    // Logged-in counsellor only
    long countByCounsellor(User counsellor);
    long countByCounsellorAndStatus(User counsellor, String status);
    long countByCounsellorAndUrgency(User counsellor, String urgency);

    // Admin (overall)
    long countByStatus(String status);
    long countByUrgency(String urgency);
}


