package webbankir.api.user_api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import webbankir.api.user_api.model.CreateUserRequest;

import java.util.Objects;

public class UserApiClient {
  
  private final RequestSpecification requestSpec;
  
  public UserApiClient(RequestSpecification requestSpec) {
    this.requestSpec = Objects.requireNonNull(requestSpec, "requestSpec must not be null");
  }
  
  public Response createUser(CreateUserRequest payload) {
    return RestAssured.given()
      .spec(requestSpec)
      .body(payload)
      .when()
      .post("/api/v1/users");
  }
  
  public Response readUser(String userId) {
    return RestAssured.given()
      .spec(requestSpec)
      .pathParam("id", userId)
      .when()
      .get("/api/v1/users/{id}");
  }
  
  public Response deleteUser(String userId) {
    return RestAssured.given()
      .spec(requestSpec)
      .pathParam("id", userId)
      .when()
      .delete("/api/v1/users/{id}");
  }
}