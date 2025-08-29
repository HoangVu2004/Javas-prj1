package AI_PRJ.WEBAPP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AI_PRJ.WEBAPP.model.Kit;

@Repository
public interface KitRepo extends JpaRepository<Kit, Long> {
    // Có sẵn:
    // - save(Kit kit): thêm/sửa
    // - findAll(): lấy danh sách
    // - findById(Long id): tìm theo ID
    // - deleteById(Long id): xoá theo ID
}