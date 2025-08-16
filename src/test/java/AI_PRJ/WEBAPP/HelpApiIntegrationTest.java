package AI_PRJ.WEBAPP;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import AI_PRJ.WEBAPP.model.Help;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
public class HelpApiIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/help";
        restTemplate = new RestTemplate();
        // Reset help data before each test by deleting all helps
        restTemplate.delete(baseUrl + "/reset");
    }

    @Test
    public void testRecordSupportAndGetCount() {
        String user = "integrationUser";
        String lab = "integrationLab";

        // Record support 1 time
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/record?user=" + user + "&lab=" + lab,
                null,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Support recorded", response.getBody());

        // Get support count, should be 1
        ResponseEntity<Integer> countResponse = restTemplate.getForEntity(
                baseUrl + "/count?user=" + user + "&lab=" + lab,
                Integer.class);
        assertEquals(HttpStatus.OK, countResponse.getStatusCode());
        assertEquals(1, countResponse.getBody());

        // Record support 4 more times (total 5)
        for (int i = 0; i < 4; i++) {
            response = restTemplate.postForEntity(
                    baseUrl + "/record?user=" + user + "&lab=" + lab,
                    null,
                    String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Support recorded", response.getBody());
        }

        // Now support count should be 5
        countResponse = restTemplate.getForEntity(
                baseUrl + "/count?user=" + user + "&lab=" + lab,
                Integer.class);
        assertEquals(HttpStatus.OK, countResponse.getStatusCode());
        assertEquals(5, countResponse.getBody());

        // Next support attempt should be forbidden
        try {
            restTemplate.postForEntity(
                    baseUrl + "/record?user=" + user + "&lab=" + lab,
                    null,
                    String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("Support limit exceeded"));
        }
    }

    @Test
    public void testGetAllHelps() {
        ResponseEntity<Help[]> response = restTemplate.getForEntity(
                baseUrl + "/all",
                Help[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println("Help table contents:");
        for (Help help : response.getBody()) {
            System.out.println("User: " + help.getUser() + ", Lab: " + help.getLab() + ", SupportCount: " + help.getSupportCount());
        }
    }

    @Test
    public void testResetHelpData() {
        // Add a record first
        String user = "resetUser";
        String lab = "resetLab";
        restTemplate.postForEntity(baseUrl + "/record?user=" + user + "&lab=" + lab, null, String.class);

        // Reset data
        ResponseEntity<String> resetResponse = restTemplate.exchange(baseUrl + "/reset", org.springframework.http.HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.OK, resetResponse.getStatusCode());
        assertEquals("Help data reset", resetResponse.getBody());

        // Verify data is empty
        ResponseEntity<Help[]> response = restTemplate.getForEntity(baseUrl + "/all", Help[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }

    @Test
    public void testRecordSupportInvalidInput() {
        // Missing user parameter
        try {
            restTemplate.postForEntity(baseUrl + "/record?lab=lab1", null, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

        // Missing lab parameter
        try {
            restTemplate.postForEntity(baseUrl + "/record?user=user1", null, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }
}
