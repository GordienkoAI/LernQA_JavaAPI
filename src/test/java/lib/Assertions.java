package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static  void assertJsonByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
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

}
