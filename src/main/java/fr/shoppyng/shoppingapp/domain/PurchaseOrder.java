package fr.shoppyng.shoppingapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_date")
	private LocalDate order_date;

	@OneToOne
	@JoinColumn(unique = true)
	private User user;

	// jhipster-needle-entity-add-field - Jhipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getOrder_date() {
		return order_date;
	}

	public PurchaseOrder order_date(LocalDate order_date) {
		this.order_date = order_date;
		return this;
	}

	public void setOrder_date(LocalDate order_date) {
		this.order_date = order_date;
	}

	public User getUser() {
		return user;
	}

	public PurchaseOrder user(User user) {
		this.user = user;
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}
	// jhipster-needle-entity-add-getters-setters - Jhipster will add getters
	// and setters here, do not remove

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PurchaseOrder purchaseOrder = (PurchaseOrder) o;
		if (purchaseOrder.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), purchaseOrder.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "PurchaseOrder{" + "id=" + getId() + ", order_date='"
				+ getOrder_date() + "'" + "}";
	}
}
