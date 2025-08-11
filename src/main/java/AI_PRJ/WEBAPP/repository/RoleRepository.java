package AI_PRJ.WEBAPP.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AI_PRJ.WEBAPP.model.Role;
import AI_PRJ.WEBAPP.model.RoleName;

/**
 * Repository cho Role entity
 * Xử lý các thao tác CRUD với cơ sở dữ liệu cho bảng roles
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Tìm vai trò theo tên (sử dụng enum RoleName)
     * @param name Tên vai trò (ADMIN, MANAGER, STAFF, CUSTOMER)
     * @return Optional<Role>
     */
    Optional<Role> findByName(RoleName name);
    
    /**
     * Kiểm tra xem vai trò có tồn tại hay không
     * @param name Tên vai trò
     * @return true nếu tồn tại, false nếu không
     */
    Boolean existsByName(RoleName name);
}