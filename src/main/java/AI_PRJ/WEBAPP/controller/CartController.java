package AI_PRJ.WEBAPP.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.dto.AddToCartRequest;
import AI_PRJ.WEBAPP.dto.ApiResponse;
import AI_PRJ.WEBAPP.model.Cart;
import AI_PRJ.WEBAPP.security.UserDetailsImpl;
import AI_PRJ.WEBAPP.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CartController {

    @Autowired
    private CartService cartService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new RuntimeException("User not authenticated or user ID not found.");
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getUserCart() {
        try {
            Long userId = getCurrentUserId();
            Cart cart = cartService.getOrCreateCartForUser(userId);
            return ResponseEntity.ok(new ApiResponse(true, "Lấy giỏ hàng thành công", cart));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addItemToCart(@RequestBody AddToCartRequest request) {
        try {
            Long userId = getCurrentUserId();
            Cart updatedCart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(new ApiResponse(true, "Thêm sản phẩm vào giỏ hàng thành công", updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateItemQuantity(@RequestBody AddToCartRequest request) {
        try {
            Long userId = getCurrentUserId();
            Cart updatedCart = cartService.updateItemQuantityInCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(new ApiResponse(true, "Cập nhật số lượng sản phẩm thành công", updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{kitId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long kitId) {
        try {
            Long userId = getCurrentUserId();
            Cart updatedCart = cartService.removeItemFromCart(userId, kitId);
            return ResponseEntity.ok(new ApiResponse(true, "Xóa sản phẩm khỏi giỏ hàng thành công", updatedCart));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }

    @PostMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> clearCart() {
        try {
            Long userId = getCurrentUserId();
            cartService.clearCart(userId);
            return ResponseEntity.ok(new ApiResponse(true, "Xóa toàn bộ giỏ hàng thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Lỗi server: " + e.getMessage()));
        }
    }
}