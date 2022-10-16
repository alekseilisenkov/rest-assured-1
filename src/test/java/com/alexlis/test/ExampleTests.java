package com.alexlis.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTests {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void getSingleUser() {
        given()
                .when()
                .get("api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"));
    }

    @Test
    void getSingleUser2() {
        String responseLastName =
        given()
                .when()
                .get("api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .extract().path("data.last_name");

        assertEquals(responseLastName, "Weaver");
    }

    @Test
    void singleUserNotFound() {
        given()
                .when()
                .get("api/users/23")
                .then()
                .statusCode(404);
    }


    @Test
    void createRequest() {
        given()
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));

    }

    @Test
    void updateByPatchRequest() {

        given()
                .body("{ \"name\": \"hasbic\", \"job\": \"blogger\"}")
                .contentType(ContentType.JSON)
                .when()
                .patch("api/users/2")
                .then()
                .statusCode(200)
                .body("name", is("hasbic"))
                .body("job", is("blogger"));
    }

    @Test
    void loginSuccessful() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        Response response =
        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("api/login")
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response.asString()).isEqualTo("{\"token\":\"QpwL5tke4Pnpja7X4\"}");
    }

    @Test
    void loginUnSuccessful() {

        String data = "{ \"email\": \"eve.holt@reqres.in\"}";
        Response response =
                given()
                        .contentType(JSON)
                        .body(data)
                        .when()
                        .post("api/login")
                        .then()
                        .statusCode(400)
                        .extract().response();

        assertThat(response.asString()).isEqualTo("{\"error\":\"Missing password\"}");
    }


}
