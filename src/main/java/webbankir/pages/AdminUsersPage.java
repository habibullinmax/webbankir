package webbankir.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class AdminUsersPage {
  
  public AdminUsersPage openPage() {
    open("/admin/users");
    return this;
  }
  
  public AdminUsersPage shouldContainUser(String name, String email) {
    SelenideElement userField = $$("#users tbody tr").findBy(text(email));
    userField.shouldBe(visible);
    userField.shouldHave(text(name));
    return this;
  }
  
  public AdminUsersPage shouldNotContainUser(String email) {
    $$("#users tbody tr").findBy(text(email)).shouldNot(exist);
    return this;
  }
}
