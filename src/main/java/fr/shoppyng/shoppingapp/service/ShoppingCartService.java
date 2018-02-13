package fr.shoppyng.shoppingapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.shoppyng.shoppingapp.domain.PurchaseOrder;
import fr.shoppyng.shoppingapp.domain.ShoppingCart;
import fr.shoppyng.shoppingapp.domain.User;
import fr.shoppyng.shoppingapp.repository.ShoppingCartRepository;

/**
 * Service Implementation for managing ShoppingCart.
 */
@Service
@Transactional
public class ShoppingCartService {

	private final Logger log = LoggerFactory
			.getLogger(ShoppingCartService.class);

	private final ShoppingCartRepository shoppingCartRepository;
	private final PurchaseOrderService purchaseOrderService;
	private final UserService userService;

	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
			PurchaseOrderService purchaseOrderService,
			UserService userService) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.purchaseOrderService = purchaseOrderService;
		this.userService = userService;
	}

	/**
	 * Save a shoppingCart.
	 *
	 * @param shoppingCart
	 *            the entity to save
	 * @return the persisted entity
	 */
	public ShoppingCart save(ShoppingCart shoppingCart) {
		log.debug("Request to save ShoppingCart : {} ", shoppingCart);
		return shoppingCartRepository.save(shoppingCart);
	}

	public ShoppingCart addProductToCart(ShoppingCart shoppingCart) {
		log.debug("Request to add Product To Cart : {}", shoppingCart);

		Optional<PurchaseOrder> purchaseOrder = getActivePurchaseOrder();
		purchaseOrder.ifPresent(order -> shoppingCart.setPurchaseOrder(order));
		shoppingCart.calculatePrice();

		return shoppingCartRepository.save(shoppingCart);
	}

	private Optional<PurchaseOrder> getActivePurchaseOrder() {
		User user = userService.getUserWithAuthorities();

		Optional<PurchaseOrder> purchaseOrder = purchaseOrderService
				.findOneByUser(user);
		if (!purchaseOrder.isPresent()) {
			PurchaseOrder newPurchaseOrder = new PurchaseOrder();
			newPurchaseOrder.setUser(user);
			newPurchaseOrder.setOrder_date(LocalDate.now());
			purchaseOrder = Optional
					.of(purchaseOrderService.save(newPurchaseOrder));
		}
		return purchaseOrder;
	}

	/**
	 * Get all the shoppingCarts.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<ShoppingCart> findAll() {
		log.debug("Request to get all ShoppingCarts");
		return shoppingCartRepository.findAll();
	}

	/**
	 * Get all the shoppingCarts to the current user.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<ShoppingCart> findAllCurrentUser() {
		log.debug("Request to get all ShoppingCarts to the current user");
		Optional<PurchaseOrder> purchaseOrder = getActivePurchaseOrder();
		return shoppingCartRepository
				.findAllByPurchaseOrder(purchaseOrder.get());
	}

	/**
	 * Get one shoppingCart by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public ShoppingCart findOne(Long id) {
		log.debug("Request to get ShoppingCart : {}", id);
		return shoppingCartRepository.findOne(id);
	}

	/**
	 * Delete the shoppingCart by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete ShoppingCart : {}", id);
		shoppingCartRepository.delete(id);
	}
}
