package AI_PRJ.WEBAPP.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AI_PRJ.WEBAPP.model.Help; // Ensure this import is correct
import AI_PRJ.WEBAPP.service.HelpService;

@RestController
@RequestMapping("/help")
public class HelpApi {

    @Autowired
    protected HelpService helpService; // Changed from private to protected

    @PostMapping("/record")
    public ResponseEntity<String> recordSupport(@RequestParam String user, @RequestParam String lab) {
        try {
            if (!helpService.canSupport(user, lab)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Support limit exceeded");
            }
            helpService.recordSupport(user, lab);
            return ResponseEntity.ok("Support recorded");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getSupportCount(@RequestParam String user, @RequestParam String lab) {
        int count = helpService.getSupportCount(user, lab);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Help>> getAllHelps() {
        List<Help> helps = helpService.getAllHelps();
        return ResponseEntity.ok(helps);
    }

    // Add reset endpoint for testing to clear all help data
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetHelpData() {
        helpService.deleteAllHelps();
        return ResponseEntity.ok("Help data reset");
    }
}
