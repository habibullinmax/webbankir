package webbankir.clients;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

public class WireMockUserLifecycleBase extends ClientTestBase {
  
  protected static final String TEST_USER_ID = "11111111-1111-1111-1111-111111111111";
  protected static final String TEST_USER_NAME = "Autotest User";
  protected static final String TEST_USER_EMAIL = "autotest@example.com";
  
  private static final String SCENARIO = "user-lifecycle";
  private static final String USER_CREATED = "USER_CREATED";
  private static final String USER_DELETED = "USER_DELETED";
  
  @SuppressWarnings("resource")
  private static WireMockServer wireMockServer;
  
  @BeforeAll
  static void startWireMock() {
    wireMockServer = new WireMockServer(options().dynamicPort());
    wireMockServer.start();
    
    configureFor("localhost", wireMockServer.port());
    
    String baseUrl = wireMockServer.baseUrl();
    
    System.setProperty("api.baseUrl", baseUrl);
    System.setProperty("ui.baseUrl", baseUrl);
    
    Configuration.baseUrl = baseUrl;
    Configuration.headless = true;
    Configuration.timeout = 5000;
    Configuration.pageLoadTimeout = 5000;
  }
  
  @AfterAll
  static void stopWireMock() {
    wireMockServer.stop();
  }
  
  @BeforeEach
  void resetWireMock() {
    resetAllRequests();
    resetAllScenarios();
    resetToDefault();
  }
  
  protected void stubUserLifecycle() {
    stubCreateUser();
    stubDeleteUserCreated();
    stubDeleteUserAlreadyDeleted();
    stubAdminUsersBeforeCreate();
    stubAdminUsersAfterCreate();
    stubAdminUsersAfterDelete();
  }
  
  private void stubCreateUser() {
    stubFor(post(urlEqualTo("/api/v1/users"))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(STARTED)
      .withRequestBody(equalToJson(resource("create-user-request.json")))
      .willReturn(aResponse()
        .withStatus(201)
        .withHeader("Content-Type", "application/json")
        .withBody(resource("create-user-response.json")))
      .willSetStateTo(USER_CREATED));
  }
  
  private void stubDeleteUserCreated() {
    stubFor(delete(urlEqualTo("/api/v1/users/" + TEST_USER_ID))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(USER_CREATED)
      .willReturn(aResponse().withStatus(204))
      .willSetStateTo(USER_DELETED));
  }
  
  private void stubDeleteUserAlreadyDeleted() {
    stubFor(delete(urlEqualTo("/api/v1/users/" + TEST_USER_ID))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(USER_DELETED)
      .willReturn(aResponse().withStatus(404)));
  }
  
  private void stubAdminUsersBeforeCreate() {
    stubFor(get(urlEqualTo("/admin/users"))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(STARTED)
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "text/html; charset=UTF-8")
        .withBody(resource("admin-users-empty.html"))));
  }
  
  private void stubAdminUsersAfterCreate() {
    stubFor(get(urlEqualTo("/admin/users"))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(USER_CREATED)
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "text/html; charset=UTF-8")
        .withBody(resource("admin-users-with-user.html"))));
  }
  
  private void stubAdminUsersAfterDelete() {
    stubFor(get(urlEqualTo("/admin/users"))
      .inScenario(SCENARIO)
      .whenScenarioStateIs(USER_DELETED)
      .willReturn(aResponse()
        .withStatus(200)
        .withHeader("Content-Type", "text/html; charset=UTF-8")
        .withBody(resource("admin-users-empty.html"))));
  }
  
  protected String resource(String path) {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new IllegalArgumentException("Resource not found: " + path);
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read resource: " + path, e);
    }
  }
}