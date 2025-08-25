package AI_PRJ.WEBAPP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AI_PRJ.WEBAPP.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    // Có sẵn:
    // - save(Category category): thêm/sửa
    // - findAll(): lấy danh sách
    // - findById(Long id): tìm theo ID
    // - deleteById(Long id): xoá theo ID
    // - existsById(Long id): kiểm tra tồn tại
}