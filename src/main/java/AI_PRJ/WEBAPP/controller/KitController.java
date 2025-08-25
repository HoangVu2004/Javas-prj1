package AI_PRJ.WEBAPP.controller;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import AI_PRJ.WEBAPP.model.Kit;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.service.FileUploadService;

import AI_PRJ.WEBAPP.model.Kit;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.service.KitService;

@RestController
@RequestMapping("/api/kits")
public class KitController {

    @Autowired
    private KitRepo KitRepo;

    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private KitService kitService;

    @GetMapping
    public List<Kit> getAllKits() {
        return KitRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kit> getKitById(@PathVariable Long id) {
        Optional<Kit> kit = KitRepo.findById(id);
        return kit.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createKit(@RequestBody Kit kit) {
        try {
            System.out.println("DEBUG: Nhận request tạo kit với categoryId = " + kit.getCategoryId());
            System.out.println("DEBUG: Kit details - Name: " + kit.getName() + ", Price: " + kit.getPrice());
            
            // Sử dụng service để có validation
            Kit savedKit = kitService.createKit(kit);
            System.out.println("DEBUG: Tạo kit thành công với ID = " + savedKit.getId());
            
            return ResponseEntity.ok(savedKit);
        } catch (IllegalArgumentException e) {
            System.out.println("DEBUG: Validation error: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.out.println("DEBUG: Unexpected error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi server: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "File không được để trống");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            String imageUrl = fileUploadService.storeFile(file);
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKit(@PathVariable Long id, @RequestBody Kit updatedKit) {
        try {
            Kit savedKit = kitService.updateKit(id, updatedKit);
            if (savedKit != null) {
                return ResponseEntity.ok(savedKit);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Kit không tìm thấy");
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi cập nhật kit: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKit(@PathVariable Long id) {
        try {
            if (kitService.deleteKit(id)) {
                Map<String, String> success = new HashMap<>();
                success.put("message", "Xóa kit thành công");
                return ResponseEntity.ok(success);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Kit không tìm thấy");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Lỗi xóa kit: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}