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

import AI_PRJ.WEBAPP.model.Lab;
import AI_PRJ.WEBAPP.repository.KitRepo;
import AI_PRJ.WEBAPP.repository.LabRepo;

@RestController
@RequestMapping("/api/labs")
public class LabController {

    @Autowired
    private LabRepo labRepository;

    @Autowired
    private KitRepo KitRepo;

    @GetMapping
    public List<Lab> getAlLabs() {
        return labRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lab> getLabById(@PathVariable Long id) {
        Optional<Lab> lab = labRepository.findById(id);
        return lab.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Lab> createLab(@RequestBody Lab lab) {
        // Optional: validate Kit existence before saving
        if (lab.getKit() != null && !KitRepo.existsById(lab.getKit().getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(labRepository.save(lab));
    }

    @PutMapping("/kit/{kitId}")
        public ResponseEntity<List<Lab>> updateLabsByKitId(@PathVariable Long kitId, @RequestBody Lab updatedLab) {
        List<Lab> labs = labRepository.findByKitId(kitId);
        if (labs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Lab lab : labs) {
            lab.setTitle(updatedLab.getTitle());
            lab.setDescription(updatedLab.getDescription());
            lab.setContent(updatedLab.getContent());
            lab.setDurationMinutes(updatedLab.getDurationMinutes());
            lab.setDifficultyLevel(updatedLab.getDifficultyLevel());
            lab.setMaxSupportCount(updatedLab.getMaxSupportCount());
            lab.setCreatedBy(updatedLab.getCreatedBy());
            lab.setKit(updatedLab.getKit());
        }

        List<Lab> updatedLabs = labRepository.saveAll(labs);
        return ResponseEntity.ok(updatedLabs);
        }
    



        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteLab(@PathVariable Long id) {
            if (labRepository.existsById(id)) {
                labRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
    }