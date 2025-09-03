package AI_PRJ.WEBAPP.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.Lab;
import AI_PRJ.WEBAPP.model.Ship;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.model.Order2;
import AI_PRJ.WEBAPP.model.OrderItem2;
import AI_PRJ.WEBAPP.repository.Order2Repository;
import AI_PRJ.WEBAPP.repository.LabRepo;
import AI_PRJ.WEBAPP.repository.ShipRepo;
import AI_PRJ.WEBAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    private ShipRepo shipRepo;

    @Autowired
    private LabRepo labRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KitRepo kitRepo;

    @Autowired
    private Order2Repository order2Repository;

    public Ship createShipment(Integer orderId) {
        Optional<Order2> optionalOrder = order2Repository.findById(orderId.longValue());
        if (optionalOrder.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng (Order) với ID: " + orderId);
        }
        Order2 order = optionalOrder.get();

        Ship ship = new Ship();
        ship.setOrderId(orderId);
        ship.setUserId(order.getCustomer().getId());
        // Lấy kitId từ order item đầu tiên. Giả định mỗi đơn hàng chỉ có 1 kit hoặc lấy kitId của item đầu tiên
        if (!order.getItems().isEmpty()) {
            ship.setKitId(order.getItems().iterator().next().getKit().getId().intValue());
        } else {
            throw new RuntimeException("Đơn hàng không có sản phẩm (Kit).");
        }
        ship.setStatus(Ship.ShipStatus.PENDING);
        ship.setCreatedAt(LocalDateTime.now());
        return shipRepo.save(ship);
    }

    public Ship updateShipmentStatus(Integer shipId, Ship.ShipStatus status) {
        System.out.println("Updating shipment " + shipId + " to status " + status);
        try {
            Optional<Ship> optionalShip = shipRepo.findById(shipId);
            if (optionalShip.isPresent()) {
                Ship ship = optionalShip.get();
                ship.setStatus(status);
                if (status == Ship.ShipStatus.DELIVERED) {
                    activateLabsForOrder(ship.getOrderId());
                }
                return shipRepo.save(ship);
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error updating shipment status: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void activateLabsForOrder(Integer orderId) {
        Optional<Order2> optionalOrder = order2Repository.findById(orderId.longValue());
        if (optionalOrder.isPresent()) {
            Order2 order = optionalOrder.get();
            for (OrderItem2 item : order.getItems()) {
                Integer kitId = item.getKit().getId().intValue();
                List<Lab> labs = labRepo.findByKitId(kitId.longValue());
                for (Lab lab : labs) {
                    lab.setActive(true);
                    labRepo.save(lab);
                    System.out.println("Activating lab: " + lab.getTitle() + " for order " + orderId);
                }
            }
        } else {
            System.err.println("Order not found for ID: " + orderId);
        }
    }

    public Optional<Ship> getShipmentStatus(Integer shipId) {
        return shipRepo.findById(shipId);
    }

    public List<Ship> getAllShipments() {
        return shipRepo.findAll();
    }
}