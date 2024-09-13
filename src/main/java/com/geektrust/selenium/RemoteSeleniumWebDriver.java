package com.geektrust.selenium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME) 
public @interface RemoteSeleniumWebDriver {
    String userEmail();
    String testName();
}
