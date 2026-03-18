package extensions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SelenideExtension implements BeforeAllCallback, AfterEachCallback {
  
  @Override
  public void beforeAll(ExtensionContext context) {
    Configuration.browser = System.getProperty("ui.browser", "chrome");
    Configuration.headless = Boolean.parseBoolean(System.getProperty("ui.headless", "true"));
    Configuration.timeout = Long.parseLong(System.getProperty("ui.timeout.ms", "10000"));
    Configuration.pageLoadTimeout = Long.parseLong(System.getProperty("ui.page.load.timeout.ms", "20000"));
    Configuration.browserSize = System.getProperty("ui.browser.size", "1440x900");
    Configuration.remote = System.getProperty("selenide.remote");
  }
  
  @Override
  public void afterEach(ExtensionContext context) {
    Selenide.closeWebDriver();
  }
}