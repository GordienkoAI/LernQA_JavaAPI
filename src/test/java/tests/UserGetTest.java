package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Get user data")
public class UserGetTest extends BaseTestCase {

    @Test
    @Description("Test not authorize get user data ")
    public void testGetUserDataNotAuth(){
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    @Description("Authorization user and get same data")
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        String[] expectedFields = {"username","firstName","lastName", "email" };
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("Get another user data")
    public void testGetAnotherUserData(){
        //1 создаем пользователя
        Map<String, String> userData = new HashMap<>();
        userData = DataGenerator.getRegistrationData(userData);
        userData.put("email", "testUser123@email.com");
        userData.put("password", "4321");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        //2 Авторизуемся под пользователем
        Response responseCheckAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", userData);

        String headers = this.getHeader(responseCheckAuth, "x-csrf-token");
        String cookie = this.getCookie(responseCheckAuth, "auth_sid");

        //3 запрашиваем данные другого пользователя
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + 2,
                        headers,
                        cookie);

        String[] unexpectedFields = {"firstName","lastName", "email" };

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);

    }
}
