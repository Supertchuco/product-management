package com.products.productmanagement.service;

import com.products.productmanagement.entity.Product;
import com.products.productmanagement.exception.*;
import com.products.productmanagement.repository.ProductRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageService imageService;

    public Product updateProductByName(String productName, Product product) {
        Collection<Product> databaseProducts = productRepository.findByProductName(productName);
        validateProductsNotFoundOnDatabase(databaseProducts, productName);
        validateRepeatedProductsOnDatabase(databaseProducts, productName);
        Product databaseProduct = databaseProducts.iterator().next();
        try {
            databaseProduct.setProductName(product.getProductName());
            databaseProduct.setProductDescription(product.getProductDescription());
            if (Objects.nonNull(product.getImages())) {
                if (CollectionUtils.isEmpty(product.getImages())) {
                    databaseProduct.getImages().clear();
                } else {
                    databaseProduct.setImages(imageService.buildImagesCollectionsToInsertProductOnDatabase(product.getImages()));
                }
            }
            databaseProduct = productRepository.save(databaseProduct);
        } catch (Exception e) {
            throw new UpdateProductException(String.format("Error to update product with name [%s]", productName));
        }
        return databaseProduct;
    }

    public Product saveNewProduct(Product product) {
        Collection<Product> databaseProducts = productRepository.findByProductName(product.getProductName());
        if (CollectionUtils.isNotEmpty(databaseProducts)) {
            throw new ProductNameAlreadyExistException(String.format("Product with this name [%s] already exist", product.getProductName()));
        } else {
            product.setParentProduct(findParentProductOnDatabase((Objects.isNull(product.getParentProduct()) ? null : product.getParentProduct().getProductName())));
            try {
                product.setImages(imageService.buildImagesCollectionsToInsertProductOnDatabase(product.getImages()));
                return productRepository.save(product);
            } catch (Exception e) {
                throw new SaveProductException(String.format("Error to save product with name [%s]", product.getProductName()));
            }
        }
    }

    public Product findProductByName(String name) {
        Collection<Product> products = productRepository.findByProductName(name);
        validateRepeatedProductsOnDatabase(products, name);
        return (products.isEmpty()) ? null : products.iterator().next();
    }

    public Collection<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProductByName(String name) {
        Collection<Product> databaseProducts = productRepository.findByProductName(name);
        validateProductsNotFoundOnDatabase(databaseProducts, name);
        validateRepeatedProductsOnDatabase(databaseProducts, name);
        try {
            productRepository.deleteByProductName(name);
        } catch (Exception e) {
            throw new DeleteProductException(String.format("Error to delete product with name [%s]", name));
        }
    }

    private void validateProductsNotFoundOnDatabase(Collection<Product> products, String productName) {
        if (CollectionUtils.isEmpty(products)) {
            throw new ProductNotFoundException(String.format("Unable to find product with name %s", productName));
        }
    }

    private void validateRepeatedProductsOnDatabase(Collection<Product> products, String productName) {
        if (CollectionUtils.size(products) > 1) {
            throw new DuplicatedProductNameOnDatabaseException(String.format(" Database Error - this product name [%s] is duplicated in database", productName));
        }
    }

    private Product findParentProductOnDatabase(String parentProductName) {
        if (StringUtils.isNotBlank(parentProductName)) {
            Collection<Product> databaseProducts = productRepository.findByProductName(parentProductName);
            if (databaseProducts.isEmpty()) {
                throw new ParentProductNotFoundException(String.format("Unable to find parent product with name %s", parentProductName));
            }
            validateRepeatedProductsOnDatabase(databaseProducts, parentProductName);
            return databaseProducts.iterator().next();
        }
        return null;
    }

}
