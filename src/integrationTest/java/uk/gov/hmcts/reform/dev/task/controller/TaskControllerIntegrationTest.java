package uk.gov.hmcts.reform.dev.task.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntegrationTest {

    @Test
    void shouldCreateAndFetchTask() {
        String id =
            given()
                .contentType("application/json")
                .body("""
                    {
                      "title": "Integration Test Task",
                      "status": "TODO",
                      "dueDate": "2026-04-27T18:00:00"
                    }
                """)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .extract()
                .path("id")
                .toString();

        given()
            .when()
            .get("/tasks/" + id)
            .then()
            .statusCode(200)
            .body("title", equalTo("Integration Test Task"));
    }

    @Test
    void shouldReturn404ForMissingTask() {
        given()
            .when()
            .get("/tasks/999")
            .then()
            .statusCode(404);
    }

    @Test
    void shouldUpdateTaskStatus() {
        Integer id =
            given()
                .contentType("application/json")
                .body("""
                {
                  "title": "Task to update",
                  "status": "TODO",
                  "dueDate": "2026-04-27T18:00:00"
                }
            """)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
            .contentType("application/json")
            .body("""
            {
              "status": "IN_PROGRESS"
            }
        """)
            .when()
            .patch("/tasks/" + id + "/status")
            .then()
            .statusCode(200)
            .body("status", equalTo("IN_PROGRESS"));
    }
    @Test
    void shouldDeleteTask() {
        Integer id =
            given()
                .contentType("application/json")
                .body("""
                {
                  "title": "Task to delete",
                  "status": "TODO",
                  "dueDate": "2026-04-27T18:00:00"
                }
            """)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
            .when()
            .delete("/tasks/" + id)
            .then()
            .statusCode(204);

        given()
            .when()
            .get("/tasks/" + id)
            .then()
            .statusCode(404);
    }

}
