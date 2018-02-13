package fr.shoppyng.shoppingapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import fr.shoppyng.shoppingapp.ShoppyngApp;
import fr.shoppyng.shoppingapp.domain.ShoppingCart;
import fr.shoppyng.shoppingapp.domain.User;
import fr.shoppyng.shoppingapp.repository.ShoppingCartRepository;
import fr.shoppyng.shoppingapp.service.PurchaseOrderService;
import fr.shoppyng.shoppingapp.service.ShoppingCartService;
import fr.shoppyng.shoppingapp.service.UserService;
import fr.shoppyng.shoppingapp.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ShoppingCartResource REST controller.
 *
 * @see ShoppingCartResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppyngApp.class)
public class ShoppingCartResourceIntTest {

	private static final Integer DEFAULT_QUANTITY = 1;
	private static final Integer UPDATED_QUANTITY = 2;

	private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
	private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

	private static final String DEFAULT_LOGIN = "johndoe";

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restShoppingCartMockMvc;

	private ShoppingCart shoppingCart;

	@Mock
	private UserService mockUserService;

	@Mock
	private PurchaseOrderService mockPurchaseOrderService;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private EntityManager em;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final ShoppingCartResource shoppingCartResource = new ShoppingCartResource(
				shoppingCartService);
		this.restShoppingCartMockMvc = MockMvcBuilders
				.standaloneSetup(shoppingCartResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

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
	@Transactional
	public void createShoppingCart() throws Exception {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

		// Create the ShoppingCart
		restShoppingCartMockMvc
				.perform(post("/api/shopping-carts")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(shoppingCart)))
				.andExpect(status().isCreated());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
		ShoppingCart testShoppingCart = shoppingCartList
				.get(shoppingCartList.size() - 1);
		assertThat(testShoppingCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(testShoppingCart.getPrice()).isEqualTo(DEFAULT_PRICE);
	}

	@Test
	@Transactional
	public void createShoppingCartWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

		// Create the ShoppingCart with an existing ID
		shoppingCart.setId(1L);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		restShoppingCartMockMvc
				.perform(post("/api/shopping-carts")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(shoppingCart)))
				.andExpect(status().isBadRequest());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllShoppingCarts() throws Exception {
		// Initialize the database
		shoppingCartRepository.saveAndFlush(shoppingCart);

		// Get all the shoppingCartList
		restShoppingCartMockMvc.perform(get("/api/shopping-carts?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id")
						.value(hasItem(shoppingCart.getId().intValue())))
				.andExpect(jsonPath("$.[*].quantity")
						.value(hasItem(DEFAULT_QUANTITY)))
				.andExpect(jsonPath("$.[*].price")
						.value(hasItem(DEFAULT_PRICE.intValue())));
	}

	@Test
	@Transactional
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void getAllShoppingCartsCurrentUser() throws Exception {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();

		// Create the ShoppingCart
		restShoppingCartMockMvc
				.perform(post("/api/shopping-carts")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(shoppingCart)))
				.andExpect(status().isCreated());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
		ShoppingCart testShoppingCart = shoppingCartList
				.get(shoppingCartList.size() - 1);

		// Get all the shoppingCartList
		restShoppingCartMockMvc.perform(get("/api/shopping-carts/currentUser"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id")
						.value(hasItem(testShoppingCart.getId().intValue())))
				.andExpect(jsonPath("$.[*].quantity")
						.value(hasItem(DEFAULT_QUANTITY)))
				.andExpect(jsonPath("$.[*].price")
						.value(hasItem(DEFAULT_PRICE.intValue())))
				.andExpect(jsonPath("$.[*].purchaseOrder.user.login")
						.value(hasItem(DEFAULT_LOGIN)));
	}

	@Test
	@Transactional
	public void getShoppingCart() throws Exception {
		// Initialize the database
		shoppingCartRepository.saveAndFlush(shoppingCart);

		// Get the shoppingCart
		restShoppingCartMockMvc
				.perform(get("/api/shopping-carts/{id}", shoppingCart.getId()))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(
						jsonPath("$.id").value(shoppingCart.getId().intValue()))
				.andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
				.andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
	}

	@Test
	@Transactional
	public void getNonExistingShoppingCart() throws Exception {
		// Get the shoppingCart
		restShoppingCartMockMvc
				.perform(get("/api/shopping-carts/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateShoppingCart() throws Exception {
		// Initialize the database
		shoppingCartService.save(shoppingCart);

		int databaseSizeBeforeUpdate = shoppingCartRepository.findAll().size();

		// Update the shoppingCart
		ShoppingCart updatedShoppingCart = shoppingCartRepository
				.findOne(shoppingCart.getId());
		updatedShoppingCart.quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

		restShoppingCartMockMvc
				.perform(put("/api/shopping-carts")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(updatedShoppingCart)))
				.andExpect(status().isOk());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeUpdate);
		ShoppingCart testShoppingCart = shoppingCartList
				.get(shoppingCartList.size() - 1);
		assertThat(testShoppingCart.getQuantity()).isEqualTo(UPDATED_QUANTITY);
		assertThat(testShoppingCart.getPrice()).isEqualTo(UPDATED_PRICE);
	}

	@Test
	@Transactional
	public void updateNonExistingShoppingCart() throws Exception {
		int databaseSizeBeforeUpdate = shoppingCartRepository.findAll().size();

		// Create the ShoppingCart

		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		restShoppingCartMockMvc
				.perform(put("/api/shopping-carts")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(shoppingCart)))
				.andExpect(status().isCreated());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteShoppingCart() throws Exception {
		// Initialize the database
		shoppingCartService.save(shoppingCart);

		int databaseSizeBeforeDelete = shoppingCartRepository.findAll().size();

		// Get the shoppingCart
		restShoppingCartMockMvc
				.perform(
						delete("/api/shopping-carts/{id}", shoppingCart.getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(ShoppingCart.class);
		ShoppingCart shoppingCart1 = new ShoppingCart();
		shoppingCart1.setId(1L);
		ShoppingCart shoppingCart2 = new ShoppingCart();
		shoppingCart2.setId(shoppingCart1.getId());
		assertThat(shoppingCart1).isEqualTo(shoppingCart2);
		shoppingCart2.setId(2L);
		assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
		shoppingCart1.setId(null);
		assertThat(shoppingCart1).isNotEqualTo(shoppingCart2);
	}

	@Test
	@Transactional
	@WithMockUser(username = DEFAULT_LOGIN, roles = { "USER" })
	public void addProductToCart() throws Exception {
		int databaseSizeBeforeCreate = shoppingCartRepository.findAll().size();
		User user = shoppingCart.getPurchaseOrder().getUser();
		when(mockUserService.getUserWithAuthorities()).thenReturn(user);
		when(mockPurchaseOrderService.findOneByUser(user))
				.thenReturn(Optional.of(shoppingCart.getPurchaseOrder()));

		// Create the ShoppingCart
		restShoppingCartMockMvc
				.perform(post("/api/shopping-carts/addProductToCart")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(shoppingCart)))
				.andExpect(status().isCreated());

		// Validate the ShoppingCart in the database
		List<ShoppingCart> shoppingCartList = shoppingCartRepository.findAll();
		assertThat(shoppingCartList).hasSize(databaseSizeBeforeCreate + 1);
		ShoppingCart testShoppingCart = shoppingCartList
				.get(shoppingCartList.size() - 1);
		assertThat(testShoppingCart.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
		assertThat(testShoppingCart.getPrice()).isEqualTo(DEFAULT_PRICE);
	}
}
