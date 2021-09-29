package cucumberOptions;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/featureFiles"},
        glue = {"initSetUp","stepDefinition"},

// glue = {"stepDefinations"}
        plugin = {"pretty",
                "json:target/reports/cucumber-reports/cucumber.json",
                "html:target/reports/cucumber-reports/cucumber.html",
                "junit:target/reports/cucumber-reports/cucumber.xml",
                "rerun:target/reports/cucumber-rerun.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome=true,
        strict=true,
// tags="@regression"
        tags="@sanity"
// "@smoke", "@orderEvent","@amar","@loadTesting","@FFM-OCMF-CreateOrderEventTransformer"
//}
)

public class TestRunner {

}
