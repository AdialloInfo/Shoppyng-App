package fr.shoppyng.shoppingapp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.shoppyng.shoppingapp.domain.PurchaseOrder;
import fr.shoppyng.shoppingapp.domain.User;
import fr.shoppyng.shoppingapp.repository.PurchaseOrderRepository;

/**
 * Service Implementation for managing PurchaseOrder.
 */
@Service
@Transactional
public class PurchaseOrderService {

	private final Logger log = LoggerFactory
			.getLogger(PurchaseOrderService.class);

	private final PurchaseOrderRepository purchaseOrderRepository;

	public PurchaseOrderService(
			PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	/**
	 * Save a purchaseOrder.
	 *
	 * @param purchaseOrder
	 *            the entity to save
	 * @return the persisted entity
	 */
	public PurchaseOrder save(PurchaseOrder purchaseOrder) {
		log.debug("Request to save PurchaseOrder : {}", purchaseOrder);
		return purchaseOrderRepository.save(purchaseOrder);
	}

	/**
	 * Get all the purchaseOrders.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<PurchaseOrder> findAll() {
		log.debug("Request to get all PurchaseOrders");
		return purchaseOrderRepository.findAll();
	}

	/**
	 * Get one purchaseOrder by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public PurchaseOrder findOne(Long id) {
		log.debug("Request to get PurchaseOrder : {}", id);
		return purchaseOrderRepository.findOne(id);
	}

	/**
	 * Get one purchaseOrder by user.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Optional<PurchaseOrder> findOneByUser(User user) {
		log.debug("Request to get PurchaseOrder by User: {}", user);
		return purchaseOrderRepository.findOptionalByUser(user);
	}

	/**
	 * Delete the purchaseOrder by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete PurchaseOrder : {}", id);
		purchaseOrderRepository.delete(id);
	}
}
