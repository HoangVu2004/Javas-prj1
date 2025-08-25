package AI_PRJ.WEBAPP.repository;

import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
    // Đếm user theo role cho báo cáo
    long countByRoles_Name(RoleName roleName);
}