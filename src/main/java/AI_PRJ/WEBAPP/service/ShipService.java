package AI_PRJ.WEBAPP.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import AI_PRJ.WEBAPP.model.Lab;
import AI_PRJ.WEBAPP.model.Ship;
import AI_PRJ.WEBAPP.repository.KitRepo;
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

    public Ship createShipment(Integer userId, Integer kitId) {
        // Kiểm tra sự tồn tại của user và kit
        userRepository.findById(userId.longValue())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        kitRepo.findById(kitId.longValue())
                .orElseThrow(() -> new RuntimeException("Error: Kit is not found."));

        Ship ship = new Ship();
        ship.setUserId(userId);
        ship.setKitId(kitId);
        ship.setStatus(Ship.ShipStatus.PENDING);
        ship.setCreatedAt(LocalDateTime.now()); // Set the creation date
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
                    activateLabsForKit(ship.getKitId());
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

    private void activateLabsForKit(Integer kitId) {
        List<Lab> labs = labRepo.findByKitId(kitId.longValue());
        for (Lab lab : labs) {
            lab.setActive(true);
            labRepo.save(lab);
            System.out.println("Activating lab: " + lab.getTitle());
        }
    }

    public Optional<Ship> getShipmentStatus(Integer shipId) {
        return shipRepo.findById(shipId);
    }

    public List<Ship> getAllShipments() {
        return shipRepo.findAll();
    }
}