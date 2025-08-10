package AI_PRJ.WEBAPP.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.Kit;
import AI_PRJ.WEBAPP.repository.KitRepo;

@RestController
@RequestMapping("/api/kits")
public class KitController {

    @Autowired
    private KitRepo KitRepo;

    @GetMapping
    public List<Kit> getAllKits() {
        return KitRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kit> getKitById(@PathVariable Long id) {
        Optional<Kit> kit = KitRepo.findById(id);
        return kit.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Kit createKit(@RequestBody Kit kit) {
        return KitRepo.save(kit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kit> updateKit(@PathVariable Long id, @RequestBody Kit updatedKit) {
        return KitRepo.findById(id)
                .map(kit -> {
                    kit.setName(updatedKit.getName());
                    kit.setDescription(updatedKit.getDescription());
                    kit.setPrice(updatedKit.getPrice());
                    kit.setStockQuantity(updatedKit.getStockQuantity());
                    kit.setCategoryId(updatedKit.getCategoryId());
                    kit.setDifficultyLevel(updatedKit.getDifficultyLevel());
                    kit.setAgeRange(updatedKit.getAgeRange());
                    kit.setCreatedBy(updatedKit.getCreatedBy());
                    return ResponseEntity.ok(KitRepo.save(kit));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKit(@PathVariable Long id) {
        if (KitRepo.existsById(id)) {
            KitRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}