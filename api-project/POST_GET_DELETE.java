package LiveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class POST_GET_DELETE {
    ResponseSpecification responseSpec;
    RequestSpecification requestSpecification;
    final static String ROOT_URI ="https://petstore.swagger.io/v2/pet" ;
    @BeforeClass
    public void Initialise()
    {
        requestSpecification =new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .addHeader("Content-Type","application/json")
                //to Set username and password
//                .addHeader("Username","Username")
//                .addHeader("password","pass")
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000l), TimeUnit.SECONDS)//reponse time is lessthan 2 seconds
                .build();


    }
    int b;
    @Test(priority = 0)
    public void POSTMethod() throws IOException {
        File f= new File("src/test/resources/input.json");
        FileInputStream inputJson= new FileInputStream(f);
        byte[] bytes=new byte[(int) f.length()];
        inputJson.read(bytes);
        String reqbody= new String(bytes, "UTF-8");
        System.out.println(reqbody);
        Response res=given().spec(requestSpecification)
                .body(reqbody)
                .when().post();
        String body=res.getBody().asPrettyString();
        inputJson.close();
        res.then().spec(responseSpec);
        b=res.then().extract().path("id");
        System.out.println(b);
    }
    @Test(priority = 1)
    public void GetMethod()
    {
        Response res=given().spec(requestSpecification)
                .pathParam("petId",b)
                .when().get(ROOT_URI+"/{petId}");
        res.then().spec(responseSpec);
        String body=res.getBody().asPrettyString();
        System.out.println(body);
        System.out.println(res.then().log().all());
    }

    @Test(priority=2)
    public void deleteMethod()
    {
        Response res=given().spec(requestSpecification)
                .pathParam("petId",b)// Set headers
                .when().delete("/{petId}"); // Send DELETE request

        res.then().spec(responseSpec);
        System.out.println("Pet ID " +b + " is deleted");

        Response res1=given().spec(requestSpecification)
                .pathParam("petId",b)
                .when().delete("/{petId}");

        res1.then().log().headers().statusCode(404);

        System.out.println("Pet ID " +b + " is already deleted and cannot be deleted");
    }


}
