package AI_PRJ.WEBAPP.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {

    private Long userId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        // Check if item already exists, if so, update quantity
        boolean found = false;
        for (CartItem existingItem : items) {
            // Use Objects.equals for null-safe comparison
            if (Objects.equals(existingItem.getKitId(), item.getKitId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
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

    // This method would require access to Kit prices, which are not yet defined.
    // For now, it's a placeholder.
    // public double getTotalPrice() {
    //     return items.stream()
    //                 .mapToDouble(item -> item.getQuantity() * getItemPrice(item.getKitId()))
    //                 .sum();
    // }

    // Placeholder for getting item price, would need to fetch from Kit service/repository
    // private double getItemPrice(Long kitId) {
    //     // Logic to fetch price for kitId
    //     return 0.0; // Placeholder
    // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(userId, cart.userId) && Objects.equals(items, cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, items);
    }

    @Override
    public String toString() {
        return "Cart{" +
               "userId=" + userId +
               ", items=" + items +
               '}';
    }
}