package fr.shoppyng.shoppingapp.cucumber.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fr.shoppyng.shoppingapp.domain.ShoppingCart;
import fr.shoppyng.shoppingapp.repository.ShoppingCartRepository;
import fr.shoppyng.shoppingapp.web.rest.ProductResourceIntTest;
import fr.shoppyng.shoppingapp.web.rest.PurchaseOrderResourceIntTest;
import fr.shoppyng.shoppingapp.web.rest.ShoppingCartResource;
import fr.shoppyng.shoppingapp.web.rest.TestUtil;

public class ShoppingCartStepDefs extends StepDefs {

	private static final String DEFAULT_LOGIN = "johndoe";
	private static final BigDecimal DEFAULT_PRICE = new BigDecimal("1.00");

	private static final Integer DEFAULT_QUANTITY = 1;

	@Autowired
	private ShoppingCartResource shoppingCartResource;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	private MockMvc restUserMockMvc;

	private ShoppingCart shoppingCart;

	private int databaseSizeBeforeCreate;

	@Autowired
	private EntityManager em;

	@Before
	public void setup() {
		this.restUserMockMvc = MockMvcBuilders
				.standaloneSetup(shoppingCartResource).build();
	}

	@Given("^a product to add in a shopping cart$")
	@Transactional
	public void a_product_to_add_in_a_shopping_cart() {
		this.shoppingCart = new ShoppingCart().quantity(DEFAULT_QUANTITY)
				.price(DEFAULT_PRICE)
				.purchaseOrder(
						PurchaseOrderResourceIntTest.createEntity(em, true))
				.product(ProductResourceIntTest.createEntity(em, true));
		this.databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();
	}

	@When("^i add a product to my shopping cart$")
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void i_add_a_product_to_my_shopping_cart() throws Throwable {
		actions = restUserMockMvc
				.perform(post("/api/shopping-carts/addProductToCart")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(
								TestUtil.convertObjectToJsonBytes(shoppingCart))
						.accept(MediaType.APPLICATION_JSON));
	}

	@Then("^adding the product is a success$")
	public void adding_the_product_is_a_success() throws Throwable {
		actions.andExpect(status().isCreated()).andExpect(
				content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Then("^the product is in the shopping cart$")
	public void the_product_is_in_the_shopping_cart() throws Throwable {
		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
		ShoppingCart testShoppingCart = shoppingCartList
				.get(shoppingCartList.size() - 1);
		assertThat(testShoppingCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(testShoppingCart.getPrice()).isEqualTo(DEFAULT_PRICE);
	}
}
