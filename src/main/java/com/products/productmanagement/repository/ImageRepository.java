package com.products.productmanagement.repository;

import com.products.productmanagement.entity.Image;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Collection<Image> findByImageType(String type);

    Collection<Image> findAll();

    Image findByImageId(int id);

    @Transactional
    void deleteByImageType(String type);

    @Transactional
    void deleteByImageId(int id);

    @Transactional
    Image save(Image image);

}


