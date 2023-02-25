package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    protected  String getHeader(Response response, String name){
        Headers header = response.getHeaders();
        assertTrue(header.hasHeaderWithName(name), "Response doesn't have header with name " +  name);
        return header.getValue(name);
    }

    protected String getCookie(Response response, String name){
        assertTrue(response.cookies().containsKey(name), "Response doesn't have cookie with name " + name);
        return response.getCookie(name);
    }

    protected int getIntFromJson(Response Response, String name){
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }
}
