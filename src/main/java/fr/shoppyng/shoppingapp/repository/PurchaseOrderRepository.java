package fr.shoppyng.shoppingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.shoppyng.shoppingapp.domain.PurchaseOrder;
import fr.shoppyng.shoppingapp.domain.User;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 */
@Repository
public interface PurchaseOrderRepository
		extends JpaRepository<PurchaseOrder, Long> {

	Optional<PurchaseOrder> findOptionalByUser(User user);
}
