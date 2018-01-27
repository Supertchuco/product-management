package com.products.productmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "Image")
@Table(name = "Image")
@NoArgsConstructor
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

    public Image(String imageType) {
        this.imageType = imageType;
    }
}
