package com.Arjun.productService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {
	@ServiceConnection
	static MongoDBContainer mongoDBContainer=new MongoDBContainer("mongo:7.0.5");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp(){
		RestAssured.baseURI="";
		RestAssured.port=port;
	}
	static {
		mongoDBContainer.start();
	}
	@Test
	void shouldCreateProduct() {
       String requestBody= """
			   {
			       "name":"realme narzo",
			       "description":"android smart phone",
			       "price":"10000"
			   }
			   """;
	   RestAssured.given()
			   .contentType("application/json")
			   .body(requestBody)
			   .when()
			   .post("/product")
			   .then()
			   .statusCode(201)
			   .body("id", Matchers.notNullValue())
			   .body("name",Matchers.equalTo("realme narzo"))
			   .body("price",Matchers.equalTo("10000"))
			   .body("description",Matchers.equalTo("android smart phone"));
	}

}
