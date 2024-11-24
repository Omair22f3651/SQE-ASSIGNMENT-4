package apiTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ProductApiTests {

    @BeforeAll
    public static void setup() {
        // Set Base URI
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // GET Test: Fetch all posts
    @Test
    public void testGetAllPosts() {
        given()
            .when()
            .get("/posts")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0)) // Ensure response contains posts
            .body("[0].userId", notNullValue()); // Validate response structure
    }

    // POST Test: Create a new post
    @Test
    public void testCreatePost() {
        given()
            .header("Content-Type", "application/json")
            .body("{ \"title\": \"foo\", \"body\": \"bar\", \"userId\": 1 }")
            .when()
            .post("/posts")
            .then()
            .statusCode(201)
            .body("id", notNullValue()) // Verify a new resource was created
            .body("title", equalTo("foo"))
            .body("body", equalTo("bar"));
    }

    // PUT Test: Update a post
    @Test
    public void testUpdatePost() {
        given()
            .header("Content-Type", "application/json")
            .body("{ \"id\": 1, \"title\": \"updated\", \"body\": \"updated body\", \"userId\": 1 }")
            .when()
            .put("/posts/1")
            .then()
            .statusCode(200)
            .body("title", equalTo("updated")) // Ensure the title is updated
            .body("body", equalTo("updated body"));
    }

    // DELETE Test: Delete a post
    @Test
    public void testDeletePost() {
        given()
            .when()
            .delete("/posts/1")
            .then()
            .statusCode(200); // Verify successful deletion
    }
}
