package fr.shoppyng.shoppingapp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * A ShoppingCart.
 */
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price", precision = 10, scale = 2)
	private BigDecimal price;

	@OneToOne
	@JoinColumn
	private PurchaseOrder purchaseOrder;

	@OneToOne
	@JoinColumn
	private Product product;

	// jhipster-needle-entity-add-field - Jhipster will add fields here, do not
	// remove
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public ShoppingCart quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public ShoppingCart price(BigDecimal price) {
		this.price = price;
		return this;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public ShoppingCart purchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
		return this;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Product getProduct() {
		return product;
	}

	public ShoppingCart product(Product product) {
		this.product = product;
		return this;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void calculatePrice() {
		this.price = this.getProduct().getPrice()
				.multiply(BigDecimal.valueOf(this.getQuantity()));
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
		ShoppingCart shoppingCart = (ShoppingCart) o;
		if (shoppingCart.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), shoppingCart.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ShoppingCart{" + "id=" + getId() + ", quantity='"
				+ getQuantity() + "'" + ", price='" + getPrice() + "'" + "}";
	}
}
