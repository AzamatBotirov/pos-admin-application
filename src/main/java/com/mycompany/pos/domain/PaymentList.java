package com.mycompany.pos.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentList.
 */
@Entity
@Table(name = "payment_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PaymentList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "summa", precision = 21, scale = 2)
    private BigDecimal summa;

    @ManyToOne
    private Product name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public PaymentList quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSumma() {
        return this.summa;
    }

    public PaymentList summa(BigDecimal summa) {
        this.setSumma(summa);
        return this;
    }

    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }

    public Product getName() {
        return this.name;
    }

    public void setName(Product product) {
        this.name = product;
    }

    public PaymentList name(Product product) {
        this.setName(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentList)) {
            return false;
        }
        return id != null && id.equals(((PaymentList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentList{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", summa=" + getSumma() +
            "}";
    }
}
