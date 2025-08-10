package AI_PRJ.WEBAPP;

import AI_PRJ.WEBAPP.service.HelpService;
import AI_PRJ.WEBAPP.repository.HelpRepo;
import AI_PRJ.WEBAPP.model.Help;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = AI_PRJ.WEBAPP.HelpSupportApplication.class)
public class HelpServiceTest {

    @Autowired
    private HelpService helpService;

    @Autowired
    private HelpRepo helpRepo;

    @BeforeEach
    public void setUp() {
        helpRepo.deleteAll();
    }

    @Test
    public void testCanSupport() {
        String user = "user1";
        String lab = "lab1";

        boolean canSupport = helpService.canSupport(user, lab);
        assertTrue(canSupport);
    }

    @Test
    public void testRecordSupportAndLimit() {
        String user = "user2";
        String lab = "lab2";

        for (int i = 0; i < 5; i++) {
            helpService.recordSupport(user, lab);
        }

        boolean canSupport = helpService.canSupport(user, lab);
        assertFalse(canSupport);

        assertThrows(IllegalStateException.class, () -> {
            helpService.recordSupport(user, lab);
        });
    }

    @Test
    public void testGetSupportCount() {
        String user = "user3";
        String lab = "lab3";

        helpService.recordSupport(user, lab);
        helpService.recordSupport(user, lab);

        int count = helpService.getSupportCount(user, lab);
        assertEquals(2, count);
    }
}
