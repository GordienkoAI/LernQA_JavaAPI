package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static  void assertJsonByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected value");
    }

    public static  void assertJsonByName(Response response, String name, String expectedValue){
        response.then().assertThat().body("$", hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "Json value is not equal to expected value");
    }

    public static void assertUserAgentData(Response resp, String expectedPlatform, String expectedBrowse, String expectedDevice){
        String actualPlatform = resp.jsonPath().getString("platform");
        String actualBrowser = resp.jsonPath().getString("browser");
        String actualDevice = resp.jsonPath().getString("device");

        assertAll("Check User Agent answer" ,
                () -> assertEquals(expectedPlatform,actualPlatform, "Platform not equal"),
                () -> assertEquals(expectedBrowse,actualBrowser, "Browser not equal"),
                () -> assertEquals(expectedDevice,actualDevice, "Device not equal"));
    }

    @Step("Проверяем ответ от сервера с ожидаемым {expectedAnswer}")
    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.print(),
                "Response text is not as expected"
        );
    }

    @Step("Проверяем отображение ошибки")
    public static void assertResponseErrorText(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.jsonPath().getString("error"),
                "Response text is not as expected"
        );
    }

    @Step("Проверяем код ответ от сервера с ожидаемым {expectedStatusCode}")
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName){
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response response, String[]  expectedFields){
        for(String expectedField : expectedFields){
            Assertions.assertJsonHasField(response, expectedField);
        }
    }

    public static void assertJsonHasNotField(Response response, String unexpectedFieldName){
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertJsonHasNotFields(Response response, String[]  expectedFields){
        for(String expectedField : expectedFields){
            Assertions.assertJsonHasNotField(response, expectedField);
        }
    }
}
