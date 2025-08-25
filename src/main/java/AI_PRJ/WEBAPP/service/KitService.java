package AI_PRJ.WEBAPP.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import AI_PRJ.WEBAPP.model.Kit;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.repository.CategoryRepo;

@Service
public class KitService {
    
    @Autowired
    private KitRepo kitRepo;
    
    @Autowired
    private CategoryRepo categoryRepo;
    
    public List<Kit> getAllKits() {
        return kitRepo.findAll();
    }
    
    public Optional<Kit> getKitById(Long id) {
        return kitRepo.findById(id);
    }
    
    public Kit createKit(Kit kit) {
        // Validate dữ liệu trước khi lưu
        validateKit(kit);
        return kitRepo.save(kit);
    }
    
    public Kit updateKit(Long id, Kit updatedKit) {
        Optional<Kit> existingKit = kitRepo.findById(id);
        if (existingKit.isPresent()) {
            Kit kit = existingKit.get();
            kit.setName(updatedKit.getName());
            kit.setDescription(updatedKit.getDescription());
            kit.setPrice(updatedKit.getPrice());
            kit.setStockQuantity(updatedKit.getStockQuantity());
            kit.setCategoryId(updatedKit.getCategoryId());
            kit.setDifficultyLevel(updatedKit.getDifficultyLevel());
            kit.setAgeRange(updatedKit.getAgeRange());
            kit.setCreatedBy(updatedKit.getCreatedBy());
            if (updatedKit.getImageUrl() != null) {
                kit.setImageUrl(updatedKit.getImageUrl());
            }
            return kitRepo.save(kit);
        }
        return null;
    }
    
    public boolean deleteKit(Long id) {
        if (kitRepo.existsById(id)) {
            kitRepo.deleteById(id);
            return true;
        }
        return false;
    }
    
    private void validateKit(Kit kit) {
        if (kit.getName() == null || kit.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên kit không được để trống");
        }
        if (kit.getPrice() == null || kit.getPrice() <= 0) {
            throw new IllegalArgumentException("Giá phải lớn hơn 0");
        }
        if (kit.getStockQuantity() == null || kit.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm");
        }
        if (kit.getDescription() == null || kit.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Mô tả không được để trống");
        }
        
        // Validate Category ID - logic linh hoạt hơn
        if (kit.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID không được để trống");
        }
        
        // Chỉ kiểm tra tồn tại trong database (không giới hạn khoảng 1-5)
        try {
            System.out.println("DEBUG: Đang kiểm tra categoryId = " + kit.getCategoryId());
            boolean exists = categoryRepo.existsById(kit.getCategoryId());
            System.out.println("DEBUG: Category " + kit.getCategoryId() + " exists = " + exists);
            
            if (!exists) {
                // Query tất cả categories để debug
                var allCategories = categoryRepo.findAll();
                System.out.println("DEBUG: Tất cả categories trong DB:");
                allCategories.forEach(cat -> System.out.println("  - ID: " + cat.getId() + ", Name: " + cat.getName()));
                
                throw new IllegalArgumentException("Category ID " + kit.getCategoryId() + " không tồn tại trong database. Kiểm tra console log để xem các category có sẵn.");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Exception khi kiểm tra category: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Lỗi khi kiểm tra Category ID: " + e.getMessage());
        }
    }
}