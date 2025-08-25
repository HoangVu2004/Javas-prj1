package AI_PRJ.WEBAPP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(Long userId) {
        this.userId = userId;
    }

    // Helper methods for managing cart items
    public void addItem(CartItem item) {
        // Check if item already exists, if so, update quantity
        boolean found = false;
        for (CartItem existingItem : items) {
            if (existingItem.getKitId().equals(item.getKitId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            item.setCart(this); // Set the parent cart
            items.add(item);
        }
    }

    public void removeItem(Long kitId) {
        items.removeIf(item -> item.getKitId().equals(kitId));
    }

    public void updateItemQuantity(Long kitId, int quantity) {
        for (CartItem item : items) {
            if (item.getKitId().equals(kitId)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    removeItem(kitId); // Remove if quantity is zero or less
                }
                break;
            }
        }
    }
}