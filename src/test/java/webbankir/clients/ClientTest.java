package webbankir.clients;

import org.junit.jupiter.api.Test;
import webbankir.api.user_api.model.UserResponse;
import webbankir.pages.AdminUsersPage;

public class ClientTest extends WireMockUserLifecycleBase {
  
  private final AdminUsersPage adminUsersPage = new AdminUsersPage();

  @Test
  void createAndDeleteUserTest() {
    stubUserLifecycle();
    
    String createdUserId = null;
    try {
      UserResponse createdUser = userApi.createUser(TEST_USER_NAME, TEST_USER_EMAIL);
      createdUserId = createdUser.id();
      adminUsersPage
        .openPage()
        .shouldContainUser(TEST_USER_NAME, TEST_USER_EMAIL);
      userApi.deleteUser(createdUser.id());
      adminUsersPage
        .openPage()
        .shouldNotContainUser(TEST_USER_EMAIL);
    } finally {
      if (createdUserId != null) {
        userApi.deleteUserIfExists(createdUserId);
      }
    }
  }
}