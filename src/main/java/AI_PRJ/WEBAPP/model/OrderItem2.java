package AI_PRJ.WEBAPP.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items2")
public class OrderItem2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order2 order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kit_id", nullable = false)
    private Kit kit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order2 getOrder() {
        return order;
    }

    public void setOrder(Order2 order) {
        this.order = order;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}