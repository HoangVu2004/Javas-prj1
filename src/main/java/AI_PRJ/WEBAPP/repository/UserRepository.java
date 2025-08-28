package AI_PRJ.WEBAPP.repository;

import java.util.List;
import java.util.Optional;

import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
    // Tìm users theo role name
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRolesName(@Param("roleName") RoleName roleName);
    // Đếm user theo role cho báo cáo
    long countByRoles_Name(RoleName roleName);
}