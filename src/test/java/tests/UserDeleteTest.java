package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


@Epic("Delete test")
public class UserDeleteTest extends BaseTestCase {

    @Test
    @Description("Delete not deleted user")
    public void testDeleteNotDeletedUser(){
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "vinkotov@example.com");
        userData.put("password", "1234");

        Response loginResp = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        String header = this.getHeader(loginResp, "x-csrf-token");
        String cookie = this.getCookie(loginResp, "auth_sid");

        Response deleteResp = apiCoreRequests
                .makeDeleteRequestWithAuthorize("https://playground.learnqa.ru/api/user/2",
                        header,
                        cookie);

        Assertions.assertResponseCodeEquals(deleteResp , 400);
        Assertions.assertResponseTextEquals(deleteResp, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }


    @Test
    @Description("Create and delete same user")
    public void testDeleteUser(){
        //1 create user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");
        System.out.println(userId);

        Assertions.assertResponseCodeEquals(responseCreateAuth , 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

        //2 login user
        Map<String, String> loginUserData = new HashMap<>();
        loginUserData.put("email", userData.get("email"));
        loginUserData.put("password", userData.get("password"));

        Response loginResp = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", loginUserData);

        String header = this.getHeader(loginResp, "x-csrf-token");
        String cookie = this.getCookie(loginResp, "auth_sid");

        // 3 deleted user
        Response deleteResp = apiCoreRequests
                .makeDeleteRequestWithAuthorize("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie);

        Assertions.assertResponseCodeEquals(deleteResp , 200);

        //4 get user information
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/"+userId);

        Assertions.assertResponseCodeEquals(responseUserData , 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    @Description("Delete user authorize another user")
    public void testDeleteUserWithAnotherAnotherUser(){
        //1 create user
        Map<String, String> firstUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", firstUserData);

        Assertions.assertResponseCodeEquals(responseCreateAuth , 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

        String userId = responseCreateAuth.jsonPath().getString("id");
        System.out.println("First user id: " + userId);

        //2 create another user
        Map<String, String> secondUserData = DataGenerator.getRegistrationData();

        Response respCreateAnotherUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", secondUserData);

        Assertions.assertResponseCodeEquals(respCreateAnotherUser , 200);
        Assertions.assertJsonHasField(respCreateAnotherUser, "id");
        String secondUserId = respCreateAnotherUser.jsonPath().getString("id");
        System.out.println("Second user id: " + secondUserId);

        //3 login another user
        Map<String, String> anotherUserData = new HashMap<>();
        anotherUserData.put("email", secondUserData.get("email"));
        anotherUserData.put("password", secondUserData.get("password"));

        Response loginResp = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", anotherUserData);

        String header = this.getHeader(loginResp, "x-csrf-token");
        String cookie = this.getCookie(loginResp, "auth_sid");

        // 3 deleted created user
        Response deleteResp = apiCoreRequests
                .makeDeleteRequestWithAuthorize("https://playground.learnqa.ru/api/user/" + userId,
                        header,
                        cookie);

        Assertions.assertResponseCodeEquals(deleteResp , 200);

        //5 get deleted user information
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId);

        Assertions.assertResponseCodeEquals(responseUserData , 200);
        Assertions.assertJsonHasField(responseUserData, "username");

    }
}
