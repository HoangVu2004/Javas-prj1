package AI_PRJ.WEBAPP.dto;

import AI_PRJ.WEBAPP.model.Ship;
import java.time.LocalDateTime;

public class ShipResponse {
    private Integer id;
    private Integer orderId;
    private Long userId;
    private Integer kitId;
    private Ship.ShipStatus status;
    private LocalDateTime createdAt;

    public ShipResponse(Ship ship) {
        this.id = ship.getId();
        this.orderId = ship.getOrderId();
        this.userId = ship.getUserId();
        this.kitId = ship.getKitId();
        this.status = ship.getStatus();
        this.createdAt = ship.getCreatedAt();
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getKitId() {
        return kitId;
    }

    public Ship.ShipStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}