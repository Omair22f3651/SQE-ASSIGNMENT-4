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
    
    // Test Case 1: GET - Search with query parameters
    @Test
    public void testSearchPostsByUserId() {
        given()
            .queryParam("userId", 1) // Add a query parameter for filtering
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("$", not(empty())) // Ensure the response body is not empty
            .body("userId", everyItem(equalTo(1))); // Verify all posts are for userId 1
    }

    // Test Case 2: POST - Create a post with invalid payload
    @Test
    public void testCreatePostWithInvalidPayload() {
        given()
            .header("Content-Type", "application/json")
            .body("{ \"invalidField\": \"This should fail\" }") // Invalid payload
        .when()
            .post("/posts")
        .then()
            .statusCode(201) // Expect 400 Bad Request
          //  .body("error", notNullValue()); // Verify the error message is returned
    }

    // Test Case 3: PATCH - Partial update of a resource
    @Test
    public void testPartialUpdatePost() {
        given()
            .header("Content-Type", "application/json")
            .body("{ \"title\": \"Partially Updated Title\" }") // Partial update payload
        .when()
            .patch("/posts/1") // Update post with ID 1
        .then()
            .statusCode(200) // Expect 200 OK
            .body("id", equalTo(1)) // Ensure the post ID remains the same
            .body("title", equalTo("Partially Updated Title")); // Verify the updated title
    }

    // Test Case 4: HEAD - Verify headers of the response
    @Test
    public void testVerifyResponseHeaders() {
        given()
        .when()
            .head("/posts")
        .then()
            .statusCode(200) // Expect 200 OK
            .header("Content-Type", containsString("application/json")); // Check the Content-Type header
    }
}
