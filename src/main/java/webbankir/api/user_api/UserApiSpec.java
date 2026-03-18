package webbankir.api.user_api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class UserApiSpec {
  
  private UserApiSpec() {
  }
  
  public static RequestSpecification userApiSpec() {
    String baseUrl = System.getProperty("api.baseUrl", "http://localhost:8080");
    int timeoutMs = Integer.parseInt(System.getProperty("api.timeout.ms", "5000"));
    
    RestAssuredConfig config = RestAssuredConfig.config()
      .httpClient(HttpClientConfig.httpClientConfig()
        .setParam("http.connection.timeout", timeoutMs)
        .setParam("http.socket.timeout", timeoutMs));
    
    return new RequestSpecBuilder()
      .setBaseUri(baseUrl)
      .setContentType(ContentType.JSON)
      .setAccept(ContentType.JSON)
      .setConfig(config)
      .addFilter(new RequestLoggingFilter())
      .addFilter(new ResponseLoggingFilter())
      .build();
  }
}
