package fr.shoppyng.shoppingapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.shoppyng.shoppingapp.ShoppyngApp;
import fr.shoppyng.shoppingapp.domain.ShoppingCart;
import fr.shoppyng.shoppingapp.repository.ShoppingCartRepository;
import fr.shoppyng.shoppingapp.web.rest.ProductResourceIntTest;
import fr.shoppyng.shoppingapp.web.rest.PurchaseOrderResourceIntTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppyngApp.class)
@Transactional
public class ShoppingCartServiceIntTest {

	private static final Integer DEFAULT_QUANTITY = 1;

	private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);

	private static final String DEFAULT_LOGIN = "johndoe";

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private EntityManager em;

	private ShoppingCart shoppingCart;

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static ShoppingCart createEntity(EntityManager em,
			boolean withPersitance) {
		ShoppingCart shoppingCart = new ShoppingCart()
				.quantity(DEFAULT_QUANTITY).price(DEFAULT_PRICE)
				.purchaseOrder(PurchaseOrderResourceIntTest.createEntity(em,
						withPersitance))
				.product(ProductResourceIntTest.createEntity(em,
						withPersitance));
		return shoppingCart;
	}

	@Before
	public void initTest() {
		shoppingCart = createEntity(em, true);
	}

	@Test
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void assertThatTheProductIsAddedToTheCartAndExistingOrder() {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

		ShoppingCart shoppingCartSaved = shoppingCartService.save(shoppingCart);
		shoppingCart = new ShoppingCart().quantity(DEFAULT_QUANTITY)
				.price(DEFAULT_PRICE)
				.product(ProductResourceIntTest.createEntity(em, true));
		ShoppingCart ShoppingCartAdded = shoppingCartService
				.addProductToCart(shoppingCart);

		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 2);
		assertThat(ShoppingCartAdded.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(ShoppingCartAdded.getPrice()).isEqualTo(DEFAULT_PRICE);
		assertThat(shoppingCartSaved.getPurchaseOrder().getId())
				.isEqualTo(ShoppingCartAdded.getPurchaseOrder().getId());

	}

	@Test
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void assertThatTheProductIsAddedToTheCartAndNewOrder() {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

		ShoppingCart ShoppingCartAdded = shoppingCartService
				.addProductToCart(shoppingCart);

		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
		assertThat(ShoppingCartAdded.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(ShoppingCartAdded.getPrice()).isEqualTo(DEFAULT_PRICE);

	}

	@Test
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void assertThatFindAllProductsFromTheCurrentUserExistingCart() {

		shoppingCartService.save(shoppingCart);
		List<ShoppingCart> listShoppingCart = shoppingCartService
				.findAllCurrentUser();

		ShoppingCart shoppingCartUser = listShoppingCart.get(0);
		assertThat(shoppingCartUser.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(shoppingCartUser.getPrice()).isEqualTo(DEFAULT_PRICE);
		assertThat(shoppingCartUser.getPurchaseOrder().getUser().getLogin())
				.isEqualTo(DEFAULT_LOGIN);

	}

}
