package AI_PRJ.WEBAPP.model;

import java.util.Objects;

public class CartItem {

    private Long kitId; // Assuming Kit is the product
    private int quantity;

    public CartItem() {
    }

    public CartItem(Long kitId, int quantity) {
        this.kitId = kitId;
        this.quantity = quantity;
    }

    public Long getKitId() {
        return kitId;
    }

    public void setKitId(Long kitId) {
        this.kitId = kitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(kitId, cartItem.kitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kitId);
    }

    @Override
    public String toString() {
        return "CartItem{" +
               "kitId=" + kitId +
               ", quantity=" + quantity +
               '}';
    }
}