
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured(){

        String url = "https://playground.learnqa.ru/api/long_redirect";
        int codeResponse = 0;

        while(codeResponse != 200) {
            Response redirectResponse = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(url)
                    .andReturn();

            url = redirectResponse.getHeader("Location");
            codeResponse = redirectResponse.getStatusCode();

            System.out.println("\n" + codeResponse);
            System.out.println(url);
        }


/*

        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responceCookies = responseForGet.getCookie("auth_cookie");

        Map<String,String> cookies = new HashMap<>();
        if (responceCookies != null) {
            cookies.put("auth_cookie",responceCookies );
        }

        Response responceForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

         responceForCheck.prettyPrint();


 */
    }

}
