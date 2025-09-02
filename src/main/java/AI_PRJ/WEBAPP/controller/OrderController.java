package AI_PRJ.WEBAPP.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.dto.CheckoutRequest;
import AI_PRJ.WEBAPP.model.Order;
import AI_PRJ.WEBAPP.model.User;
import AI_PRJ.WEBAPP.repository.UserRepository;
import AI_PRJ.WEBAPP.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    private User getDemoUser() {
        // For demo purposes, use a default user (e.g., admin with ID 1)
        // In a real application, you would handle unauthenticated users differently
        return userRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException(
                        "Demo user not found in database. Please ensure user with ID 1 exists."));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createOrder(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            User user = getDemoUser();
            Order order = orderService.createOrderFromCart(user.getId(), checkoutRequest.getAddress(),
                    checkoutRequest.getPhone(), checkoutRequest.getPaymentMethod(), checkoutRequest.getName());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getOrders() {
        try {
            User user = getDemoUser();
            List<Order> orders = orderService.getOrdersForUser(user.getId());
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
