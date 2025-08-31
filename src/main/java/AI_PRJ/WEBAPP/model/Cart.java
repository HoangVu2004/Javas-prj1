package AI_PRJ.WEBAPP.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Cart(User user) {
        this.user = user;
    }

    // Helper methods for managing cart items
    public void addItem(CartItem item) {
        // Check if item already exists, if so, update quantity
        boolean found = false;
        for (CartItem existingItem : items) {
            if (existingItem.getKit().getId().equals(item.getKit().getId())) {
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
        items.removeIf(item -> item.getKit().getId().equals(kitId));
    }

    public void updateItemQuantity(Long kitId, int quantity) {
        for (CartItem item : items) {
            if (item.getKit().getId().equals(kitId)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    removeItem(kitId); // Remove if quantity is zero or less
                }
                break;
            }
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}