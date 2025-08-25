package AI_PRJ.WEBAPP.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kit_id", nullable = false)
    private Long kitId; // Assuming Kit is the product

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public CartItem() {
    }

    public CartItem(Long kitId, int quantity) {
        this.kitId = kitId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(kitId, cartItem.kitId) && Objects.equals(cart, cartItem.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kitId, cart);
    }

    @Override
    public String toString() {
        return "CartItem{" +
               "id=" + id +
               ", kitId=" + kitId +
               ", quantity=" + quantity +
               ", cartId=" + (cart != null ? cart.getId() : "null") +
               '}';
    }
}