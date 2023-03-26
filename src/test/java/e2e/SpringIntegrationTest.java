package e2e;

import com.parking.model.ParkingTicket;
import io.cucumber.spring.CucumberTestContext;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.parking.constants.ApplicationConstants.PARKING_EP;
import static com.parking.constants.ApplicationConstants.PARK_EP;
import static com.parking.constants.ApplicationConstants.V1;

@Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
public class SpringIntegrationTest {

    static ResponseEntity<ParkingTicket> latestResponse = null;
    static String EP = "http://localhost:9090/api" + V1 + PARKING_EP + PARK_EP;
    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;

    void executePost() throws IOException {
        System.out.println("Execute Post");
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        latestResponse = restTemplate.exchange(EP + "/cars", HttpMethod.POST, null, ParkingTicket.class);
    }

}
