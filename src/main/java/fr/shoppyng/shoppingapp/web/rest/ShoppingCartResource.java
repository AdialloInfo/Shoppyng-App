package fr.shoppyng.shoppingapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.shoppyng.shoppingapp.domain.ShoppingCart;
import fr.shoppyng.shoppingapp.service.ShoppingCartService;
import fr.shoppyng.shoppingapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ShoppingCart.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCartResource {

	private final Logger log = LoggerFactory
			.getLogger(ShoppingCartResource.class);

	private static final String ENTITY_NAME = "shoppingCart";

	private final ShoppingCartService shoppingCartService;

	public ShoppingCartResource(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	/**
	 * POST /shopping-carts : Create a new shoppingCart.
	 *
	 * @param shoppingCart
	 *            the shoppingCart to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new shoppingCart, or with status 400 (Bad Request) if the
	 *         shoppingCart has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/shopping-carts")
	@Timed
	public ResponseEntity<ShoppingCart> createShoppingCart(
			@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
		log.debug("REST request to save ShoppingCart : {}", shoppingCart);
		if (shoppingCart.getId() != null) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
							"idexists", "A new shoppingCart cannot already have an ID"))
					.body(null);
		}
		ShoppingCart result = shoppingCartService.save(shoppingCart);
		return ResponseEntity
				.created(new URI("/api/shopping-carts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
						result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /shopping-carts : Updates an existing shoppingCart.
	 *
	 * @param shoppingCart
	 *            the shoppingCart to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         shoppingCart, or with status 400 (Bad Request) if the
	 *         shoppingCart is not valid, or with status 500 (Internal Server
	 *         Error) if the shoppingCart couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/shopping-carts")
	@Timed
	public ResponseEntity<ShoppingCart> updateShoppingCart(
			@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
		log.debug("REST request to update ShoppingCart : {}", shoppingCart);
		if (shoppingCart.getId() == null) {
			return createShoppingCart(shoppingCart);
		}
		ShoppingCart result = shoppingCartService.save(shoppingCart);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
						shoppingCart.getId().toString()))
				.body(result);
	}

	/**
	 * GET /shopping-carts : get all the shoppingCarts.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         shoppingCarts in body
	 */
	@GetMapping("/shopping-carts")
	@Timed
	public List<ShoppingCart> getAllShoppingCarts() {
		log.debug("REST request to get all ShoppingCarts");
		return shoppingCartService.findAll();
	}

	/**
	 * GET /shopping-carts : get all the shoppingCarts to the current user.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         shoppingCarts in body
	 */
	@GetMapping("/shopping-carts/currentUser")
	@Timed
	public List<ShoppingCart> getAllShoppingCartsCurrentUser() {
		log.debug("REST request to get all ShoppingCarts");
		return shoppingCartService.findAllCurrentUser();
	}

	/**
	 * GET /shopping-carts/:id : get the "id" shoppingCart.
	 *
	 * @param id
	 *            the id of the shoppingCart to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         shoppingCart, or with status 404 (Not Found)
	 */
	@GetMapping("/shopping-carts/{id}")
	@Timed
	public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable Long id) {
		log.debug("REST request to get ShoppingCart : {}", id);
		ShoppingCart shoppingCart = shoppingCartService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shoppingCart));
	}

	/**
	 * DELETE /shopping-carts/:id : delete the "id" shoppingCart.
	 *
	 * @param id
	 *            the id of the shoppingCart to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/shopping-carts/{id}")
	@Timed
	public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) {
		log.debug("REST request to delete ShoppingCart : {}", id);
		shoppingCartService.delete(id);
		String message = "Product has been deleted to the shopping cart";
		return ResponseEntity.ok().headers(HeaderUtil.createAlert(message, ""))
				.build();
	}

	/**
	 * POST /shopping-carts/addProductToCart : Add product to shopping cart
	 *
	 * @param shoppingCart
	 *            the shoppingCart to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new shoppingCart, or with status 400 (Bad Request) if the
	 *         shoppingCart has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/shopping-carts/addProductToCart")
	@Timed
	public ResponseEntity<ShoppingCart> addProductToCart(
			@RequestBody ShoppingCart shoppingCart) throws URISyntaxException {
		log.debug("REST request to add Product To Cart : {}", shoppingCart);

		ShoppingCart result = shoppingCartService
				.addProductToCart(shoppingCart);
		String message = result.getProduct().getName()
				+ " has been added to the shopping cart";

		return ResponseEntity
				.created(new URI("/api/shopping-carts/" + result.getId()))
				.headers(HeaderUtil.createAlert(message,
						result.getProduct().getName()))
				.body(result);
	}
}
