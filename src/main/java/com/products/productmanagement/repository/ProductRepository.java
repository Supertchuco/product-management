package com.products.productmanagement.repository;

import com.products.productmanagement.entity.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Collection<Product> findByProductName(String Name);

    Collection<Product> findAll();

    Product findByProductId(int id);

    @Transactional
    void deleteByProductName(String name);

    @Transactional
    void deleteByProductId(int id);

    @Transactional
    Product save(Product product);

}
