

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RestAssuredTests {

    @ParameterizedTest
    @ValueSource(strings = {"", "John" , "Pete"})
    public void testHelloMethodWithoutName(String name){
        Map<String, String> paramName = new HashMap<>();

        if(name.length() > 0){
            paramName.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(paramName)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer not existed");
    }


    @Test
    public void testRestAssured() throws InterruptedException {

//Ex9: Подбор пароля

        Map<String, String> testData = new HashMap<>();

        String cookie;
        int code;

        try {
            BufferedReader in = new BufferedReader(new FileReader("src/test/resources/testData"));
            String str;

            while ((str = in.readLine()) != null) {
                testData.put("login", "super_admin");
                testData.put("password", str);
                System.out.println("\n" + str);

                Response getCookie = RestAssured
                        .given()
                        .body(testData)
                        .when()
                        .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                        .andReturn();

                 getCookie.prettyPrint();


                cookie = getCookie.getCookie("auth_cookie");
                System.out.println("\n" + cookie);

                Response checkAuth = RestAssured
                        .given()
                        .queryParam("auth_cookie", cookie)
                        .when()
                        .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                        .andReturn();
                checkAuth.prettyPrint();

                String answer = checkAuth.htmlPath().getString("body");
                checkAuth.prettyPrint();

                if (answer.equals("You are authorized")) {
                    System.out.println("Правильный пароль: " + str);
                    break;

                }
            }
            in.close();
    } catch (IOException e) {}









/*  Ex8: Токены ===========================================
        Map<String, String> data = new HashMap<>();

        Response  startWork = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        startWork.prettyPrint();

        String accessToken = startWork.path("token");
        int secondsResponse = startWork.path("seconds");

        data.put("token", accessToken);

        Response getResults = RestAssured
                .given()
                .queryParam("token", accessToken)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        getResults.prettyPrint();

        String status = getResults.path("status");
        Assertions.assertEquals( "Job is NOT ready" , status);

        Thread.sleep(secondsResponse * 1000);

         getResults = RestAssured
                .given()
                .queryParam("token", accessToken)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        getResults.prettyPrint();

        status = getResults.path("status");
        String result = getResults.path("result");

        Assertions.assertEquals( "Job is ready" , status);
        Assertions.assertNotEquals(null, result);


   Ex7: Долгий редирект ======================================

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
*/

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
