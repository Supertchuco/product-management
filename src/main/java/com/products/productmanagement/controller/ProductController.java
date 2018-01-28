package com.products.productmanagement.controller;

import com.products.productmanagement.entity.Product;
import com.products.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping(value = "/findByName/{productName}", method = RequestMethod.GET)
    public @ResponseBody
    Product findProductByName(@PathVariable("productName") String productName) {
        return productService.findProductByName(productName);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public @ResponseBody
    Collection<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @RequestMapping(value = "/deleteByName/{productName}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProductByName(@PathVariable("productName") String productName) {
        productService.deleteProductByName(productName);
        return new ResponseEntity("Product deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveNewProduct(product);
    }

    @RequestMapping(value = "/editByName/{productName}", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Product editProduct(@PathVariable("productName") String productName, @RequestBody Product product) {
        return productService.updateProductByName(productName, product);
    }

}
