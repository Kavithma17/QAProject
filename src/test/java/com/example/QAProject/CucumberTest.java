package com.example.QAProject;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.example.QAProject"  // include package of step definitions & CucumberSpringConfiguration
)
public class CucumberTest {
}
