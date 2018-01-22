package com.products.productmanagement.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity(name = "product")
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    @Column
    private int productId;

    @Column
    private String productName;

    @Column
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private Collection<Image> images;

/*    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="parentProduct")
    @JsonManagedReference
    private Product parentProduct;*/

/*
    @ManyToOne(cascade = { CascadeType.ALL })
    @JsonManagedReference
    @JoinTable(name = "parent_product", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = {
            @JoinColumn(name = "parent_product_id") })
    private Product parentProduct;
*/


    public Product(String productName, String productDescription) {
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public Product() {
    }
}
