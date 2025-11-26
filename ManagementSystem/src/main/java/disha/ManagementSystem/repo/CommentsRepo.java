package disha.ManagementSystem.repo;

import disha.ManagementSystem.entity.Comment;
import disha.ManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByCounsellor(User loggedUser);
}
