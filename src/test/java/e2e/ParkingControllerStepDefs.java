package e2e;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ParkingControllerStepDefs extends SpringIntegrationTest {

    @When("^the customer park the car$")
    public void customer_park_the_car() throws Exception {
        System.out.println("Execute Post");
        executePost();
    }

    @Then("^the customer receives status code of (\\d+)$")
    public void the_customer_receives_status_code_of(int statusCode) throws Throwable {
        final Integer currentStatusCode = latestResponse.getStatusCode().value();
        assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode, is(statusCode));
    }

    @And("^the customer receives the parking ticket number$")
    public void the_customer_receives_server_version_body() throws Throwable {
        Assertions.assertNotNull(latestResponse.getBody().getTicketNumber());
    }
}
