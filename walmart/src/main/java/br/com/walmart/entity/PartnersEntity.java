package br.com.walmart.entity;

import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "PARTNERES")
@Getter
public class PartnersEntity implements Serializable {

    private static final long serialVersionUID = 7237699050276973379L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Pattern(regexp = "^([^[0-9]\\p{Punct}|\\s])(.*)")
    @NotBlank(message = "Required field")
    @NotNull(message = "Required field")
    @Column(name = "NAME", unique = true)
    private String partnersName;

    @Pattern(regexp = "^([^[0-9]\\p{Punct}|\\s])(.*)")
    @NotBlank(message = "Required field")
    @NotNull(message = "Required field")
    @Column(name = "AGE", unique = true)
    private String productName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPartnersName() {return partnersName;}

    public void setPartnersName(String partnersName) {
        this.partnersName = partnersName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String toString() {
        return "PartnersEntity [id=" + id + ", PartnersEntity name: " + partnersName + ", Product Name: " + productName + "]";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartnersEntity partners = (PartnersEntity) o;

        if (id != partners.id) return false;
        if (!partnersName.equals(partners.partnersName)) return false;
        return productName.equals(partners.productName);
    }

    @Override
    public int hashCode() {int result = (int) (id ^ (id >>> 32));result = 31 * result + partnersName.hashCode();
        result = 31 * result + productName.hashCode();
        return result;}
}
