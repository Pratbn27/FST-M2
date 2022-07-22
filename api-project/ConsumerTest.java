package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Header
    Map<String,String> headerReq=new HashMap<>();

    //API Resource path

    String resourcePath="/api/users";
    //creating the PAct

    @Pact(consumer = "UserConsumer",provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){

        //set headers
        headerReq.put("Content-Type","application/json");

        //Create Reuestbdy
        DslPart reqBody=new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        //create responsebody
        DslPart resBody=new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");


        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(headerReq)
                    .body(reqBody)
                .willRespondWith()
                    .status(201)
                    .body(resBody)


//                .uponReceiving("Request to get a user")
//                    .method("GET")
//                    .path(resourcePath)
//                            .headers(headerReq)
//                    .body(reqBody)
//                .willRespondWith()
//                    .status(201)
//                    .body(resBody)
                .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider",port="8282")
    public void Consumer_Test(){

        String baseURI="http://localhost:8282";
        Map<String,Object> reqBody=new HashMap<>();
        reqBody.put("id",12);
        reqBody.put("firstName","Prathibha");
        reqBody.put("lastName","BN");
        reqBody.put("email","abc@hij.com");
        Response res=given().headers(headerReq).body(reqBody)
                .when().post(baseURI+resourcePath);

        System.out.println(res.getBody().asPrettyString());

    }



}
