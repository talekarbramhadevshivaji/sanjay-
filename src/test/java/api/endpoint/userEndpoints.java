package api.endpoint;

import api.payload.user;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class userEndpoints {
    
    public static Response createuser(user payload) {
        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(Routes.post_url);
        return response;
    }

    public static Response getuser(String userName) {
        Response response = given()
            .accept(ContentType.JSON)
            .pathParam("username", userName) // 🔹 Fixed incorrect pathParam
        .when()
            .get(Routes.get_url);
        return response;
    }

    public static Response putuser(String userName, user payload) {
        Response response = given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .pathParam("username", userName) // 🔹 Fixed incorrect pathParam
            .body(payload)
        .when()
            .put(Routes.put_url);
        return response;
    }

    public static Response deleteuser(String userName) {
        Response response = given()
            .accept(ContentType.JSON)
            .pathParam("username", userName) // 🔹 Fixed incorrect pathParam
        .when()
            .delete(Routes.del_url);
        return response;
    }
}
