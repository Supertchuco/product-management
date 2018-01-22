package com.products.productmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "Image")
@Table(name = "Image")
public class Image {

    @Id
    @GeneratedValue
    @Column
    private int imageId;

    @Column
    private String imageType;

    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonBackReference
    private Product product;

/*    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;*/

/*    @ManyToMany(mappedBy = "images")
    @JsonBackReference
    private List<Product> products;*/

/*    @ManyToOne
    @JoinTable(name="product_images",
            joinColumns={@JoinColumn(name="product_id")},
            inverseJoinColumns={@JoinColumn(name="images_id")})
    private Product product;*/

/*
    @ManyToOne
    @JoinTable(name = "MY_JOIN_TABLE",
            joinColumns = {@JoinColumn(name = "MY_ENTITY_B_FK", insertable = false,
                    updatable = false, referencedColumnName = "myIdB")},
            inverseJoinColumns = {@JoinColumn(name = "MY_ENTITY_A_FK", insertable = false,
                    updatable = false, referencedColumnName = "myIdA")}
    )
    private Product product;
*/

    public Image(String imageType) {
        this.imageType = imageType;
    }

    public Image() {
    }
}
