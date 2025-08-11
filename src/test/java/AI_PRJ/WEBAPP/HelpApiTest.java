package AI_PRJ.WEBAPP;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import AI_PRJ.WEBAPP.controller.HelpApi;
import AI_PRJ.WEBAPP.service.HelpService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = AI_PRJ.WEBAPP.HelpSupportApplication.class)
public class HelpApiTest {

    @Autowired
    private HelpApi helpApi;

    private HelpService helpService;

    @BeforeEach
    public void setUp() throws Exception {
        helpService = Mockito.mock(HelpService.class);
        // Use reflection to set private field helpService in helpApi
        java.lang.reflect.Field field = HelpApi.class.getDeclaredField("helpService");
        field.setAccessible(true);
        field.set(helpApi, helpService);
    }

    @Test
    public void testRecordSupportSuccess() {
        String user = "user1";
        String lab = "lab1";

        when(helpService.canSupport(user, lab)).thenReturn(true);
        doNothing().when(helpService).recordSupport(user, lab);

        ResponseEntity<String> response = helpApi.recordSupport(user, lab);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Support recorded", response.getBody());
    }

    @Test
    public void testRecordSupportLimitExceeded() {
        String user = "user2";
        String lab = "lab2";

        when(helpService.canSupport(user, lab)).thenReturn(false);

        ResponseEntity<String> response = helpApi.recordSupport(user, lab);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Support limit exceeded", response.getBody());
    }

    @Test
    public void testRecordSupportThrowsException() {
        String user = "user4";
        String lab = "lab4";

        when(helpService.canSupport(user, lab)).thenReturn(true);
        doThrow(new IllegalStateException("Support limit exceeded")).when(helpService).recordSupport(user, lab);

        ResponseEntity<String> response = helpApi.recordSupport(user, lab);
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Support limit exceeded", response.getBody());
    }

    @Test
    public void testGetSupportCount() {
        String user = "user3";
        String lab = "lab3";

        when(helpService.getSupportCount(user, lab)).thenReturn(3);

        ResponseEntity<Integer> response = helpApi.getSupportCount(user, lab);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, response.getBody());
    }
}
