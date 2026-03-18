package webbankir.api.user_api;

import webbankir.api.user_api.model.CreateUserRequest;
import webbankir.api.user_api.model.UserResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserApiSteps {
  private final UserApiClient client;
  
  public UserApiSteps(UserApiClient client) {
    this.client = client;
  }
  
  public UserResponse createUser(String name, String email) {
    CreateUserRequest payload = new CreateUserRequest(name, email);
    
    UserResponse created = client.createUser(payload)
      .then()
      .statusCode(201)
      .extract()
      .as(UserResponse.class);
    
    assertThat(created.name(), equalTo(name));
    assertThat(created.email(), equalTo(email));
    
    return created;
  }
  
  public UserResponse readUser(String userId) {
    return client.readUser(userId)
      .then()
      .statusCode(200)
      .extract()
      .as(UserResponse.class);
  }
  
  public void deleteUser(String userId) {
    client.deleteUser(userId)
      .then()
      .statusCode(204);
  }
  
  public void deleteUserIfExists(String userId) {
    client.deleteUser(userId)
      .then()
      .statusCode(anyOf(is(204), is(404)));
  }
}
