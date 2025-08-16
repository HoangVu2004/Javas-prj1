package AI_PRJ.WEBAPP.controller;

import AI_PRJ.WEBAPP.model.Ship;
import AI_PRJ.WEBAPP.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ship")
public class ShipApi {

    @Autowired
    private ShipService shipService;

    @PostMapping
    public Ship createShipment(@RequestParam Integer userId, @RequestParam Integer kitId) {
        return shipService.createShipment(userId, kitId);
    }

    @PutMapping("/{shipId}/status")
    public ResponseEntity<Ship> updateShipmentStatus(@PathVariable Integer shipId, @RequestParam Ship.ShipStatus status) {
        Ship updatedShip = shipService.updateShipmentStatus(shipId, status);
        if (updatedShip != null) {
            return ResponseEntity.ok(updatedShip);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{shipId}/status")
    public ResponseEntity<Ship.ShipStatus> getShipmentStatus(@PathVariable Integer shipId) {
        Optional<Ship> ship = shipService.getShipmentStatus(shipId);
        return ship.map(value -> ResponseEntity.ok(value.getStatus()))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/lab/{labId}/access")
    public ResponseEntity<String> canAccessLab(@RequestParam Integer userId) {
        return ResponseEntity.ok("Lab access granted."); 
    }
}
