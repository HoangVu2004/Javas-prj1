// package AI_PRJ.WEBAPP.model;

// import jakarta.persistence.*;
// import lombok.Getter;
// import lombok.Setter;

// import java.util.Objects;

// @Entity
// @Table(name = "cart_items")
// @Getter
// @Setter
// public class CartItem {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "cart_id", nullable = false)
//     private Cart cart;

//     @ManyToOne
//     @JoinColumn(name = "kit_id", nullable = false)
//     private Kit kit;

//     @Column(nullable = false)
//     private int quantity;

//     public CartItem() {
//     }

//     public CartItem(Cart cart, Kit kit, int quantity) {
//         this.cart = cart;
//         this.kit = kit;
//         this.quantity = quantity;
//     }

//     @Override
//     public boolean equals(Object o) {
//         if (this == o) return true;
//         if (o == null || getClass() != o.getClass()) return false;
//         CartItem cartItem = (CartItem) o;
//         return Objects.equals(id, cartItem.id);
//     }

//     @Override
//     public int hashCode() {
//         return Objects.hash(id);
//     }
// }