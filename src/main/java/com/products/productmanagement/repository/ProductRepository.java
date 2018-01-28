package com.products.productmanagement.repository;

import com.products.productmanagement.entity.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Collection<Product> findByProductName(String Name);

    Collection<Product> findAll();

    @Transactional
    void deleteByProductName(String name);

    @Transactional
    Product save(Product product);

}
