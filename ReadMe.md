# LambdaTest Remote Selenium Web Driver

This is a Java library that can be used by a Java Selenium project to run their tests on a remote server in LambdaTest Selenium Grid.

## How to use this library?

Import this library in your maven project as a dependency.

```
<dependency>
  <groupId>com.geektrust.selenium</groupId>
  <artifactId>lambdatest-remote-selenium-webdriver</artifactId>
  <packaging>jar</packaging>
  <version>1.0.0</version>
</dependency>
```

Add the LambdaTest creddentials and URL as environment variables.

```
LT_USERNAME=
LT_ACCESS_KEY=
LT_REMOTE_URL=
```

### Using in Main.java

Create your Selenium script within a package `com.geektrust.selenium` and add the following code.

```java
@Component
public class Main {

    @Autowired
    private RemoteSeleniumWebDriverAspect aspect;

    @RemoteSeleniumWebDriver(
        testName = "${SELENIUM_TEST_NAME:defaultTest}",
        userEmail = "${USER_EMAIL:defaultEmail}"
    )
    public WebDriver webDriver() {
        return aspect.getRemoteWebDriver();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(WebDriverConfig.class);
        Main app = context.getBean(Main.class);
        WebDriver driver = app.webDriver();

        // Continue with your selenium tests after this.
    }
}
```

While running the application ensure that the environment variables are set.

### Using in JUnit test

Create your JUnit test within the package `com.selenium.geektrust` and add the following code. 

```java
@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySeleniumJUnitTest {

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
    ApplicationContext context = new AnnotationConfigApplicationContext(RemoteWebDriverConfig.class);
    MySeleniumJUnitTest app = context.getBean(MySeleniumJUnitTest.class);
    this.driver = app.webDriver();
  }

  @Test
  public void testYourWebApp() {
   //Use this.driver to start writing your selenium tests
  }

  @AfterEach
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}

```