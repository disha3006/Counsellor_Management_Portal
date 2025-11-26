package disha.ManagementSystem.repo;

import disha.ManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long > {

        Optional<User> findByEmail(String email);
        List<User> findByRole(String role);
}
