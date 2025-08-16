package AI_PRJ.WEBAPP.service;

import AI_PRJ.WEBAPP.model.Lab;
import AI_PRJ.WEBAPP.model.Ship;
import AI_PRJ.WEBAPP.repository.LabRepo;
import AI_PRJ.WEBAPP.repository.ShipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    private ShipRepo shipRepo;

    @Autowired
    private LabRepo labRepo;

    public Ship createShipment(Integer userId, Integer kitId) {
        Ship ship = new Ship();
        ship.setUserId(userId);
        ship.setKitId(kitId);
        ship.setStatus(Ship.ShipStatus.PENDING);
        return shipRepo.save(ship);
    }

    public Ship updateShipmentStatus(Integer shipId, Ship.ShipStatus status) {
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
    }

    private void activateLabsForKit(Integer kitId) {

        List<Lab> labs = labRepo.findByKitId(kitId.longValue());
        for (Lab lab : labs) {
            System.out.println("Activating lab: " + lab.getTitle());
        }
    }

    public Optional<Ship> getShipmentStatus(Integer shipId) {
        return shipRepo.findById(shipId);
    }
}
