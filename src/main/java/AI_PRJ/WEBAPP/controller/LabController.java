package AI_PRJ.WEBAPP.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.Lab;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.repository.LabRepo;

/**
 * =============================================
 * LAB CONTROLLER - QUẢN LÝ BÀI LAB STEM
 * =============================================
 * 
 * Phân quyền theo vai trò:
 * - ADMIN, MANAGER: Full CRUD operations
 * - STAFF: Xem Labs để hỗ trợ khách hàng
 * - CUSTOMER: Chỉ xem Labs của KIT đã mua (cần implement business logic)
 * 
 * Business Logic quan trọng:
 * - Lab chỉ được kích hoạt khi KIT đã giao thành công
 * - Customer chỉ truy cập được Lab của KIT đã mua
 */
@RestController
@RequestMapping("/api/labs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LabController {

    @Autowired
    private LabRepo labRepository;

    @Autowired
    private KitRepo kitRepo;

    /**
     * Xem tất cả Labs
     * Truy cập: ADMIN, MANAGER (để quản lý), STAFF (để hỗ trợ)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<?> getAllLabs() {
        try {
            List<Lab> labs = labRepository.findAll();
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy danh sách Labs thành công", labs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xem chi tiết một Lab
     * Truy cập: ADMIN, MANAGER, STAFF (để hỗ trợ)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<?> getLabById(@PathVariable Long id) {
        try {
            Optional<Lab> lab = labRepository.findById(id);
            
            if (lab.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy Lab với ID: " + id));
            }
            
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy thông tin Lab thành công", lab.get()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Customer truy cập Labs của KIT đã mua
     * Truy cập: CUSTOMER (với logic kiểm tra đã mua KIT)
     */
    @GetMapping("/my-labs/{kitId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getMyLabs(@PathVariable Long kitId) {
        try {
            // TODO: Implement logic kiểm tra:
            // 1. Customer đã mua KIT này chưa?
            // 2. KIT đã được giao thành công chưa?
            // 3. Chỉ khi cả 2 điều kiện đúng thì mới cho truy cập Labs
            
            List<Lab> labs = labRepository.findByKitId(kitId);
            
            if (labs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy Labs cho KIT ID: " + kitId));
            }
            
            // TEMPORARY: Trả về labs (cần implement business logic)
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy Labs của bạn thành công", labs));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Tạo Lab mới
     * Truy cập: Chỉ ADMIN và MANAGER
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createLab(@RequestBody Lab lab) {
        try {
            // Validate Kit existence
            if (lab.getKit() != null && !kitRepo.existsById(lab.getKit().getId())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "KIT không tồn tại với ID: " + lab.getKit().getId()));
            }
            
            // Validate required fields
            if (lab.getTitle() == null || lab.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Tiêu đề Lab không được để trống"));
            }
            
            Lab savedLab = labRepository.save(lab);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Tạo Lab thành công", savedLab));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Cập nhật Labs theo KIT ID
     * Truy cập: Chỉ ADMIN và MANAGER
     */
    @PutMapping("/kit/{kitId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateLabsByKitId(@PathVariable Long kitId, @RequestBody Lab updatedLab) {
        try {
            List<Lab> labs = labRepository.findByKitId(kitId);
            
            if (labs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy Labs cho KIT ID: " + kitId));
            }

            for (Lab lab : labs) {
                if (updatedLab.getTitle() != null) lab.setTitle(updatedLab.getTitle());
                if (updatedLab.getDescription() != null) lab.setDescription(updatedLab.getDescription());
                if (updatedLab.getContent() != null) lab.setContent(updatedLab.getContent());
                if (updatedLab.getDurationMinutes() != 0) lab.setDurationMinutes(updatedLab.getDurationMinutes());
                if (updatedLab.getDifficultyLevel() != null) lab.setDifficultyLevel(updatedLab.getDifficultyLevel());
                if (updatedLab.getMaxSupportCount() != 0) lab.setMaxSupportCount(updatedLab.getMaxSupportCount());
                if (updatedLab.getCreatedBy() != null) lab.setCreatedBy(updatedLab.getCreatedBy());
                if (updatedLab.getKit() != null) lab.setKit(updatedLab.getKit());
            }

            List<Lab> updatedLabs = labRepository.saveAll(labs);
            return ResponseEntity.ok(new ApiResponse(true, 
                "Cập nhật Labs thành công", updatedLabs));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xóa Lab
     * Truy cập: Chỉ ADMIN và MANAGER
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> deleteLab(@PathVariable Long id) {
        try {
            if (!labRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy Lab với ID: " + id));
            }
            
            labRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Xóa Lab thành công"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * STAFF xem Labs để hỗ trợ khách hàng
     * Truy cập: STAFF, MANAGER, ADMIN
     */
    @GetMapping("/{id}/help")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getLabForSupport(@PathVariable Long id) {
        try {
            Optional<Lab> lab = labRepository.findById(id);
            
            if (lab.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy Lab với ID: " + id));
            }
            
            // Trả về thông tin Lab để STAFF có thể hỗ trợ
            return ResponseEntity.ok(new ApiResponse(true, 
                "Lấy thông tin Lab để hỗ trợ thành công", lab.get()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Inner class for API Response
     */
    private static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}