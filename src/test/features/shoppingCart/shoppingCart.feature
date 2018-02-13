Feature: Shopping cart

    Scenario: Add a product to a shopping cart
    	Given a product to add in a shopping cart
        When i add a product to my shopping cart
        Then adding the product is a success
        And the product is in the shopping cart