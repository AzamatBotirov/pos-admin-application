package com.mycompany.pos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MarketBase.
 */
@Entity
@Table(name = "market_base")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MarketBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "current_price", precision = 21, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne
    private Product name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarketBase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public MarketBase quantity(String quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public MarketBase price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCurrentPrice() {
        return this.currentPrice;
    }

    public MarketBase currentPrice(BigDecimal currentPrice) {
        this.setCurrentPrice(currentPrice);
        return this;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public ZonedDateTime getCreateDate() {
        return this.createDate;
    }

    public MarketBase createDate(ZonedDateTime createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public MarketBase date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Product getName() {
        return this.name;
    }

    public void setName(Product product) {
        this.name = product;
    }

    public MarketBase name(Product product) {
        this.setName(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketBase)) {
            return false;
        }
        return id != null && id.equals(((MarketBase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketBase{" +
            "id=" + getId() +
            ", quantity='" + getQuantity() + "'" +
            ", price=" + getPrice() +
            ", currentPrice=" + getCurrentPrice() +
            ", createDate='" + getCreateDate() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
