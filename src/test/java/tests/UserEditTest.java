package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    @Test
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

        //3 редактирование пользователя
        String newName = "Change name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth,"x-csrf-token" ),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);


        //4 просмотр данных
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth,"x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        responseUserData.prettyPrint();
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }
}
