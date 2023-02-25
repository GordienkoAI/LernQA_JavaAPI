package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeWork extends BaseTestCase {

//Ex12: Тест запроса на метод header
    @Test
    public void checkHeaderRequest(){
        Response resp = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();


        String headerValue = this.getHeader(resp, "x-secret-homework-header");
        if(headerValue != null){
            System.out.println(headerValue);
        }
    }






    //Ex11: Тест запроса на метод cookie
    @Test
    public void checkCookieRequest(){
        Response resp = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Headers headers = resp.getHeaders();

        String value = this.getCookie(resp,"Home");
        System.out.println(value);
    }



// Ex10: Тест на короткую фразу
    @ParameterizedTest
    @ValueSource(strings = {"1234567890123456", "123456789012"})
    public void checkStringLength(String value){
        assertTrue(value.length() > 15, "Value length lees 15 simbols");
    }
}
