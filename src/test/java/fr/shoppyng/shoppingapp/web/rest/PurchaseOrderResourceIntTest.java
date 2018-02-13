package fr.shoppyng.shoppingapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import fr.shoppyng.shoppingapp.ShoppyngApp;
import fr.shoppyng.shoppingapp.domain.PurchaseOrder;
import fr.shoppyng.shoppingapp.repository.PurchaseOrderRepository;
import fr.shoppyng.shoppingapp.service.PurchaseOrderService;
import fr.shoppyng.shoppingapp.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the PurchaseOrderResource REST controller.
 *
 * @see PurchaseOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppyngApp.class)
public class PurchaseOrderResourceIntTest {

	private static final LocalDate DEFAULT_ORDER_DATE = LocalDate
			.ofEpochDay(0L);
	private static final LocalDate UPDATED_ORDER_DATE = LocalDate
			.now(ZoneId.systemDefault());

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPurchaseOrderMockMvc;

	private PurchaseOrder purchaseOrder;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final PurchaseOrderResource purchaseOrderResource = new PurchaseOrderResource(
				purchaseOrderService);
		this.restPurchaseOrderMockMvc = MockMvcBuilders
				.standaloneSetup(purchaseOrderResource)
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
	public static PurchaseOrder createEntity(EntityManager em,
			boolean withPersitance) {
		PurchaseOrder purchaseOrder = new PurchaseOrder()
				.order_date(DEFAULT_ORDER_DATE)
				.user(UserResourceIntTest.createEntity(em, withPersitance));
		if (withPersitance) {
			em.persist(purchaseOrder);
		}
		return purchaseOrder;
	}

	@Before
	public void initTest() {
		purchaseOrder = createEntity(em, true);
	}

	@Test
	@Transactional
	public void createPurchaseOrder() throws Exception {
		int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();
		purchaseOrder = new PurchaseOrder().order_date(DEFAULT_ORDER_DATE);

		// Create the PurchaseOrder
		restPurchaseOrderMockMvc
				.perform(post("/api/purchase-orders")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(purchaseOrder)))
				.andExpect(status().isCreated());

		// Validate the PurchaseOrder in the database
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository
				.findAll();
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate + 1);
		PurchaseOrder testPurchaseOrder = purchaseOrderList
				.get(purchaseOrderList.size() - 1);
		assertThat(testPurchaseOrder.getOrder_date())
				.isEqualTo(DEFAULT_ORDER_DATE);
	}

	@Test
	@Transactional
	public void createPurchaseOrderWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

		// Create the PurchaseOrder with an existing ID
		purchaseOrder = new PurchaseOrder().order_date(DEFAULT_ORDER_DATE);
		purchaseOrder.setId(1L);

		// An entity with an existing ID cannot be created, so this API call
		// must fail
		restPurchaseOrderMockMvc
				.perform(post("/api/purchase-orders")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(purchaseOrder)))
				.andExpect(status().isBadRequest());

		// Validate the PurchaseOrder in the database
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository
				.findAll();
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllPurchaseOrders() throws Exception {
		// Initialize the database
		purchaseOrderRepository.saveAndFlush(purchaseOrder);

		// Get all the purchaseOrderList
		restPurchaseOrderMockMvc
				.perform(get("/api/purchase-orders?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id")
						.value(hasItem(purchaseOrder.getId().intValue())))
				.andExpect(jsonPath("$.[*].order_date")
						.value(hasItem(DEFAULT_ORDER_DATE.toString())));
	}

	@Test
	@Transactional
	public void getPurchaseOrder() throws Exception {
		// Initialize the database
		purchaseOrderRepository.saveAndFlush(purchaseOrder);

		// Get the purchaseOrder
		restPurchaseOrderMockMvc
				.perform(
						get("/api/purchase-orders/{id}", purchaseOrder.getId()))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id")
						.value(purchaseOrder.getId().intValue()))
				.andExpect(jsonPath("$.order_date")
						.value(DEFAULT_ORDER_DATE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingPurchaseOrder() throws Exception {
		// Get the purchaseOrder
		restPurchaseOrderMockMvc
				.perform(get("/api/purchase-orders/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePurchaseOrder() throws Exception {
		// Initialize the database
		purchaseOrderService.save(purchaseOrder);

		int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

		// Update the purchaseOrder
		PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository
				.findOne(purchaseOrder.getId());
		updatedPurchaseOrder.order_date(UPDATED_ORDER_DATE);

		restPurchaseOrderMockMvc
				.perform(put("/api/purchase-orders")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(
								updatedPurchaseOrder)))
				.andExpect(status().isOk());

		// Validate the PurchaseOrder in the database
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository
				.findAll();
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
		PurchaseOrder testPurchaseOrder = purchaseOrderList
				.get(purchaseOrderList.size() - 1);
		assertThat(testPurchaseOrder.getOrder_date())
				.isEqualTo(UPDATED_ORDER_DATE);
	}

	@Test
	@Transactional
	public void updateNonExistingPurchaseOrder() throws Exception {
		int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

		// Create the PurchaseOrder
		purchaseOrder = new PurchaseOrder().order_date(DEFAULT_ORDER_DATE);
		// If the entity doesn't have an ID, it will be created instead of just
		// being updated
		restPurchaseOrderMockMvc
				.perform(put("/api/purchase-orders")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil
								.convertObjectToJsonBytes(purchaseOrder)))
				.andExpect(status().isCreated());

		// Validate the PurchaseOrder in the database
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository
				.findAll();
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePurchaseOrder() throws Exception {
		// Initialize the database
		purchaseOrderService.save(purchaseOrder);

		int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

		// Get the purchaseOrder
		restPurchaseOrderMockMvc
				.perform(delete("/api/purchase-orders/{id}",
						purchaseOrder.getId())
								.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository
				.findAll();
		assertThat(purchaseOrderList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(PurchaseOrder.class);
		PurchaseOrder purchaseOrder1 = new PurchaseOrder();
		purchaseOrder1.setId(1L);
		PurchaseOrder purchaseOrder2 = new PurchaseOrder();
		purchaseOrder2.setId(purchaseOrder1.getId());
		assertThat(purchaseOrder1).isEqualTo(purchaseOrder2);
		purchaseOrder2.setId(2L);
		assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);
		purchaseOrder1.setId(null);
		assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);
	}
}
