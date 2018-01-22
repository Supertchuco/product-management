package com.products.productmanagement.repository;

import com.products.productmanagement.entity.Image;
import com.products.productmanagement.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public DatabaseLoader(ProductRepository productRepo, ImageRepository imageRepo) {
        this.productRepository = productRepo;
        this.imageRepository = imageRepo;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.productRepository.save(new Product("Tecate", "cerveja"));
        this.productRepository.save(new Product("Tang", "suco"));
        this.productRepository.save(new Product("Golly", "suco"));

        this.imageRepository.save(new Image("png"));
        this.imageRepository.save(new Image("jpeg"));

    }
}
