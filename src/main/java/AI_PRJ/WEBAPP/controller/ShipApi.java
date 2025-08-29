package AI_PRJ.WEBAPP.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import AI_PRJ.WEBAPP.security.UserPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.dto.ApiResponse;
import AI_PRJ.WEBAPP.model.Ship;
import AI_PRJ.WEBAPP.service.ShipService;

/**
 * =============================================
 * SHIPMENT API - QUẢN LÝ GIAO NHẬN KIT VẬT LÝ
 * =============================================
 *
 * Phân quyền :
 * - MANAGER: Quản lý giao nhận Kit vật lý (theo dõi, cập nhật trạng thái)
 * - ADMIN: Toàn quyền
 * - STAFF: Xem thông tin để hỗ trợ khách hàng
 * - CUSTOMER: Chỉ xem trạng thái giao hàng của mình
 */
@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ShipApi {

    @Autowired
    private ShipService shipService;

    /**
    
     * Truy cập: ADMIN, MANAGER (quản lý giao nhận)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createShipment(@RequestParam Integer userId, @RequestParam Integer kitId) {
        try {
            Ship createdShipment = shipService.createShipment(userId, kitId);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Tạo shipment thành công", createdShipment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Cập nhật trạng thái shipment 
     * Truy cập: ADMIN, MANAGER (quản lý giao nhận)
     */
    @PutMapping("/{shipId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> updateShipmentStatus(@PathVariable Integer shipId, @RequestParam String status) {
        try {
            Ship.ShipStatus shipStatus;
            try {
                shipStatus = Ship.ShipStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Trạng thái không hợp lệ: " + status));
            }

            Ship updatedShipment = shipService.updateShipmentStatus(shipId, shipStatus);
            
            if (updatedShipment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy shipment với ID: " + shipId));
            }
            
            String message = "Cập nhật trạng thái shipment thành công";
            if (shipStatus == Ship.ShipStatus.DELIVERED) {
                message += " - Labs đã được kích hoạt!";
            }
            
            return ResponseEntity.ok(new ApiResponse(true, message, updatedShipment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    /**
     * Xem trạng thái shipment
     * Truy cập: ADMIN, MANAGER, STAFF (để hỗ trợ), CUSTOMER (xem của mình)
     */
    @GetMapping("/{shipId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('STAFF') or hasRole('CUSTOMER')")
    public ResponseEntity<?> getShipmentStatus(@PathVariable Integer shipId) {
        try {
            Optional<Ship> shipmentOpt = shipService.getShipmentStatus(shipId);
            
            if (shipmentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Không tìm thấy shipment với ID: " + shipId));
            }

            // Kiểm tra quyền sở hữu cho CUSTOMER
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                Long currentUserId = userPrincipal.getId();
                Long shipUserId = shipmentOpt.get().getUserId().longValue();
                if (!currentUserId.equals(shipUserId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Bạn không có quyền xem thông tin giao hàng này."));
                }
            }
            
            return ResponseEntity.ok(new ApiResponse(true,
                "Lấy trạng thái shipment thành công", shipmentOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }


    /**
     * Lấy danh sách tất cả shipments
     * Truy cập: ADMIN, MANAGER
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> getAllShipments() {
        try {
            List<Ship> shipments = shipService.getAllShipments();
            return ResponseEntity.ok(new ApiResponse(true, "Lấy danh sách shipment thành công", shipments));
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