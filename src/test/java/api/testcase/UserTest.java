package api.testcase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoint.userEndpoints;
import api.payload.user;
import io.restassured.response.Response;

public class UserTest {
    Faker faker;
    user userPayload;

    @BeforeClass
    public void generateTestData() {
        faker = new Faker();
        userPayload = new user();
        userPayload.setId(faker.number().numberBetween(1000, 9999));
        userPayload.setUsername(faker.name().username().replace(".", ""));
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(6, 12));
        userPayload.setPhone(faker.phoneNumber().cellPhone());
    }

    @Test(priority = 1)
    public void testCreateUser() {
        Response response = userEndpoints.createuser(userPayload);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200, "User creation failed");
    }

    @Test(priority = 2)
    public void testGetUser() {
        // Retry mechanism to ensure API consistency
        int retries = 3;
        int delay = 2000;

        Response response = null;
        for (int i = 0; i < retries; i++) {
            response = userEndpoints.getuser(userPayload.getUsername());
            if (response.getStatusCode() == 200) {
                break;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200, "User retrieval failed");
    }

    @Test(priority = 3)
    public void testUpdateUser() {
        // Generate new values for update
        String updatedFirstName = faker.name().firstName();
        userPayload.setFirstName(updatedFirstName);

        // Send update request
        Response updateResponse = userEndpoints.putuser(userPayload.getUsername(), userPayload);
        updateResponse.then().log().all();
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "User update request failed");

        // Retry mechanism to ensure update reflection
        int retries = 3;
        int delay = 2000;
        Response getResponse = null;
        boolean updateVerified = false;

        for (int i = 0; i < retries; i++) {
            getResponse = userEndpoints.getuser(userPayload.getUsername());
            getResponse.then().log().all();

            if (getResponse.getStatusCode() == 200) {
                String retrievedFirstName = getResponse.jsonPath().getString("firstName");
                if (retrievedFirstName.equals(updatedFirstName)) {
                    updateVerified = true;
                    break;
                }
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Assert.assertTrue(updateVerified, "User update not reflected in GET response");
    }

    @Test(priority = 4)
    public void testDeleteUser() {
        // Send delete request
        Response response = userEndpoints.deleteuser(userPayload.getUsername());
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200, "User deletion failed");

        // Retry mechanism to validate deletion
        int retries = 3;
        int delay = 2000;
        Response getResponse = null;
        boolean userDeleted = false;

        for (int i = 0; i < retries; i++) {
            getResponse = userEndpoints.getuser(userPayload.getUsername());
            getResponse.then().log().all();

            if (getResponse.getStatusCode() == 404) {
                userDeleted = true;
                break;
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Assert.assertTrue(userDeleted, "User still exists after deletion");
    }
}