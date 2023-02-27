package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Edit user data")
public class UserEditTest extends BaseTestCase {

    @Test
    @Description("Edit just created user data")
    public void testEditJustCreatedTest(){
        //1 создание пользователя
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestReturnJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //2 авторизация
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String authHeader = this.getHeader(responseGetAuth,"x-csrf-token" );
        String authCookie = this.getCookie(responseGetAuth, "auth_sid");

        //3 редактирование пользователя
        String newName = "Change name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        authHeader,
                        authCookie,
                editData);

        //4 просмотр данных
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        authHeader,
                        authCookie);

        responseUserData.prettyPrint();
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("Update user data by not authorize")
    public void testUpdateUserDataNotAuthorize(){
        //1 create user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestReturnJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //2 update person date
        userData.put("lastName", "Barabulka");
        Response responseUpdateData = apiCoreRequests.makePutRequestNotAuthorize("https://playground.learnqa.ru/api/user/" + userId,
                userData);

        Assertions.assertResponseTextEquals(responseUpdateData, "Auth token not supplied");
    }

    @Test
    @Description("Update user data authorize another user")
    public void testUpdateUserDataAuthorizeAnotherUser(){
        //1 create user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestReturnJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //2authorize another user
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        String header = this.getHeader(responseGetAuth,"x-csrf-token");

        //3 update person date authorize another person
        userData.put("lastName", "Barabulka");
        Response responseUpdateData = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                header,
                cookie,
                userData);

        Assertions.assertResponseTextEquals(responseUpdateData, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Update user mail authorize some user")
    public void testUpdateUserMailAuthorizeSomeUser(){
        //1 create user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestReturnJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        //2 authorize user

        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/api/user/login", userData);
        String cookie = this.getCookie(responseGetAuth,"auth_sid");
        String header = this.getHeader(responseGetAuth,"x-csrf-token");

        //3 update person date authorize another person
        userData.put("email", "Barabulkamail.com");
        Response responseUpdateData = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                header,
                cookie,
                userData);

        responseUpdateData.print();
        Assertions.assertResponseTextEquals(responseUpdateData, "Invalid email format");
    }
}
