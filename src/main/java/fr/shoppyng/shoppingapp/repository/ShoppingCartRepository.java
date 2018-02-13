package fr.shoppyng.shoppingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.shoppyng.shoppingapp.domain.PurchaseOrder;
import fr.shoppyng.shoppingapp.domain.ShoppingCart;

/**
 * Spring Data JPA repository for the ShoppingCart entity.
 */
@Repository
public interface ShoppingCartRepository
		extends JpaRepository<ShoppingCart, Long> {

	List<ShoppingCart> findAllByPurchaseOrder(PurchaseOrder purchaseOrder);
}
