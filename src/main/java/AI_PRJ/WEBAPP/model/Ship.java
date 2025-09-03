package AI_PRJ.WEBAPP.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "deliveries")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderId; // New field for order ID
    private Long userId; // New field for user ID
    private Integer kitId; // New field for kit ID

    @Enumerated(EnumType.STRING)
    private ShipStatus status;

    private LocalDateTime createdAt;

    public enum ShipStatus {
        PENDING,
        DELIVERED
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getKitId() {
        return kitId;
    }

    public void setKitId(Integer kitId) {
        this.kitId = kitId;
    }

    public ShipStatus getStatus() {
        return status;
    }

    public void setStatus(ShipStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
