package webbankir.clients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import extensions.SelenideExtension;
import webbankir.api.user_api.UserApiSpec;
import webbankir.api.user_api.UserApiClient;
import webbankir.api.user_api.UserApiSteps;

@ExtendWith({
  SelenideExtension.class
})
public abstract class ClientTestBase {
  protected UserApiClient apiClient;
  protected UserApiSteps userApi;
  
  @BeforeEach
  void setUpApi() {
    this.apiClient = new UserApiClient(UserApiSpec.userApiSpec());
    this.userApi = new UserApiSteps(apiClient);
  }
}