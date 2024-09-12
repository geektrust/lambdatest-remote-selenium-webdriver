package com.geektrust.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

  @Value("${PROJECT_NAME}")
  private String projectName;

  @Value("${PLATFORM_NAME}")
  private String platformName;

  @Value("${BROWSER_VERSION}")
  private String browserVersion;

  @Around("@annotation(webDriverInit)")
  public Object initWebDriver(
    ProceedingJoinPoint joinPoint,
    RemoteSeleniumWebDriver webDriverInit
  ) throws Throwable {
    ChromeOptions browserOptions = new ChromeOptions();
    browserOptions.setPlatformName("Windows 10");
    browserOptions.setBrowserVersion("128");

    String[] tags = new String[] { userEmail, testName };
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

    browserOptions.setCapability("LT:Options", ltOptions);
    
    try {
      remoteWebDriver =
        new RemoteWebDriver(
          new URL(System.getenv("LT_REMOTE_URL")),
          browserOptions
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
