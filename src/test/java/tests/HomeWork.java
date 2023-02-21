package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork extends BaseTestCase {

//Ex11: Тест запроса на метод cookie
    @Test
    public void checkCookieRequest(){
        Response resp = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        this.getIntFromJson(resp, "cookie");
    }



// Ex10: Тест на короткую фразу
    @ParameterizedTest
    @ValueSource(strings = {"1234567890123456", "123456789012"})
    public void checkStringLength(String value){
        assertTrue(value.length() > 15, "Value length lees 15 simbols");
    }
}
