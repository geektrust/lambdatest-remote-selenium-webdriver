package com.geektrust.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RemoteSeleniumWebDriverTest {

  @Autowired
  private RemoteSeleniumWebDriverAspect aspect;

  private WebDriver driver;

  @RemoteSeleniumWebDriver(
    testName = "${SELENIUM_TEST_NAME:defaultTest}",
    userEmail = "${USER_EMAIL:defaultEmail}"
  )
  public WebDriver webDriver() {
    return aspect.getRemoteWebDriver();
  }

  @BeforeEach
  public void setUp() {
    ApplicationContext context = new AnnotationConfigApplicationContext(
      RemoteWebDriverConfig.class
    );
    RemoteSeleniumWebDriverTest app = context.getBean(
      RemoteSeleniumWebDriverTest.class
    );
    this.driver = app.webDriver();
  }

  @Test
  public void testGoogle() {
    driver.get("https://www.google.com");
    String actualTitle = driver.getTitle();
    String expectedTitle = "Google";
    assertEquals(expectedTitle, actualTitle);
  }

  @AfterEach
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
