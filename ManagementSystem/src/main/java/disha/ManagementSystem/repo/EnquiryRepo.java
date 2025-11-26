package disha.ManagementSystem.repo;

import disha.ManagementSystem.entity.Enquiry;
import disha.ManagementSystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepo extends JpaRepository<Enquiry, Long> {

    Page<Enquiry> findByCounsellorId(Long userId, Pageable pageable);

    List<Enquiry> findByEmail(String email);

    List<Enquiry> findByCounsellor(User loggedUser);

    long countByStatus(String status);

    long countByUrgency(String urgency);

}

