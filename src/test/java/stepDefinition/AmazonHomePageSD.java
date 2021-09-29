package stepDefinition;



import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import webPages.AmazonHomePage;

import static initSetUp.SetUP.amazonHomePage;


public class AmazonHomePageSD extends AmazonHomePage {

    @Given("the user lands to the Amazon home page")

    public void the_user_lands_to_the_amazon_home_page() {

    System.out.println(amazonHomePage.getCurrentPageTitle());

    }
    @When("user enter {string}")
    public void user_enter(String input) {

        SearchBox.sendKeys(input);
        SearchBoxButton.click();
    }
    @Then("user should see a corespending {string}")
    public void user_should_see_a_corespending(String string) {

        System.out.println(driver.getTitle());
    }

}
