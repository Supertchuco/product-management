package com.products.productmanagement.service;

import com.products.productmanagement.entity.Image;
import com.products.productmanagement.exception.*;
import com.products.productmanagement.repository.ImageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public Image updateImageByType(String type, Image image) {
        Collection<Image> databaseImages = imageRepository.findByImageType(type);
        validateImagesFoundOnDatabase(databaseImages, type);
        Image databaseImage = databaseImages.iterator().next();
        try {
            databaseImage.setImageType(image.getImageType());
            databaseImage = imageRepository.save(databaseImage);
        } catch (Exception e) {
            throw new UpdateImageException(String.format("Error to update image with type [%s]", type));
        }
        return databaseImage;
    }

    public Image saveNewImage(Image image) {
        Collection<Image> databaseImages = imageRepository.findByImageType(image.getImageType());
        if (CollectionUtils.isNotEmpty(databaseImages)) {
            throw new ImageTypeAlreadyExistException(String.format("Image with this type [%s] already exist", image.getImageType()));
        }
        try {
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new SaveImageException(String.format("Unable to save image with type %s", image.getImageType()));
        }
    }

    public Collection<Image> findImageByType(String type) {
        Collection<Image> images = imageRepository.findByImageType(type);
        validateImagesFoundOnDatabase(images, type);
        return images;
    }

    public Collection<Image> findAllImages() {
        return imageRepository.findAll();
    }

    public void deleteImageByType(String type) {
        Collection<Image> databaseImages = imageRepository.findByImageType(type);
        validateImagesFoundOnDatabase(databaseImages, type);
        try {
            imageRepository.deleteByImageType(type);
        } catch (Exception e) {
            throw new DeleteImageException(String.format("Unable to delete image with type %s", type));
        }
    }

    public Collection<Image> buildImagesCollectionsToInsertProductOnDatabase(Collection<Image> images) {
        Collection<Image> imagesReturn = new ArrayList<Image>();
        if (CollectionUtils.isNotEmpty(images)) {
            images.stream().forEach(image -> {
                Collection<Image> imagesDB = imageRepository.findByImageType(image.getImageType());
                imagesReturn.add((CollectionUtils.isNotEmpty(imagesDB)) ? imagesDB.iterator().next() : image);
            });
        }
        return imagesReturn;
    }

    private void validateImagesFoundOnDatabase(Collection<Image> images, String imageType) {
        if (CollectionUtils.isEmpty(images)) {
            throw new ImageNotFoundException(String.format("Unable to find image with this type [%s]", imageType));
        } else if (CollectionUtils.size(images) > 1) {
            throw new DuplicatedImageTypeOnDatabaseException(String.format(" Database Error - this image type [%s] is duplicated in database", imageType));
        }
    }
}
