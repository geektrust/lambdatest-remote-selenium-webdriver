package com.geektrust.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RemoteSeleniumWebDriverAspect {

  private WebDriver remoteWebDriver;

  @Value("${USER_EMAIL}")
  private String userEmail;

  @Value("${SELENIUM_TEST_NAME}")
  private String testName;

  @Value("${PROJECT_NAME:Geektrust Selenium Test}")
  private String projectName;

  @Value("${PLATFORM_NAME:Windows 10}")
  private String platformName;

  @Value("${BROWSER_VERSION:128}")
  private String browserVersion;

  @Around("@annotation(webDriverInit)")
  public Object initWebDriver(
    ProceedingJoinPoint joinPoint,
    RemoteSeleniumWebDriver webDriverInit
  ) throws Throwable {
    String[] tags = new String[] { userEmail, testName };
    MutableCapabilities w3cCapabilities = new MutableCapabilities();
    w3cCapabilities.setCapability("browserName", "chrome");
    w3cCapabilities.setCapability("browserVersion", "128.0");
    w3cCapabilities.setCapability("platformName", "Windows 10"); // Example for platform

    HashMap<String, Object> ltOptions = new HashMap<String, Object>();
    ltOptions.put("username", System.getenv("LT_USERNAME"));
    ltOptions.put("accessKey", System.getenv("LT_ACCESS_KEY"));
    ltOptions.put("visual", true);
    ltOptions.put("video", true);
    ltOptions.put("console", true);
    ltOptions.put("terminal", true);
    ltOptions.put("devicelog", true);
    ltOptions.put("build", userEmail + "-" + testName);
    ltOptions.put("project", projectName);
    ltOptions.put("name", testName);
    ltOptions.put("selenium_version", "4.0.0");
    ltOptions.put("w3c", true);
    ltOptions.put("tags", tags);
    w3cCapabilities.setCapability("LT:Options", ltOptions);

    try {
      System.out.println(System.getenv("LT_REMOTE_URL"));
      remoteWebDriver =
        new RemoteWebDriver(
          new URL(System.getenv("LT_REMOTE_URL")),
          w3cCapabilities
        );
      Object result = joinPoint.proceed();
      return result;
    } catch (MalformedURLException e) {
      throw e;
    }
  }

  public WebDriver getRemoteWebDriver() {
    return this.remoteWebDriver;
  }
}
