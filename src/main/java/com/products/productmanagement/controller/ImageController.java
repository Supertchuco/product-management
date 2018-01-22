package com.products.productmanagement.controller;

import com.products.productmanagement.entity.Image;
import com.products.productmanagement.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @RequestMapping(value = "/findByType/{imageType}", method = RequestMethod.GET)
    public @ResponseBody
    Collection<Image> findProductByType(@PathVariable("imageType") String type) {
        return imageService.findImageByType(type);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public @ResponseBody
    Collection<Image> findAllImages() {
        return imageService.findAllImages();
    }

    @RequestMapping(value = "/deleteByType/{imageType}", method = RequestMethod.DELETE)
    public ResponseEntity deleteImageByName(@PathVariable("imageType") String type) {
        imageService.deleteImageByType(type);
        return new ResponseEntity("Image deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Image> saveImage(@RequestBody Image image) {
        return new ResponseEntity<Image>(imageService.saveNewImage(image), HttpStatus.OK);
    }

    @RequestMapping(value = "/editByType/{imageType}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Image editImage(@PathVariable("imageType") String imageType, @RequestBody Image image) {
        return imageService.updateImageByType(imageType, image);
    }

}
