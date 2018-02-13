package fr.shoppyng.shoppingapp.cucumber.stepdefs;

import fr.shoppyng.shoppingapp.ShoppyngApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ShoppyngApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
