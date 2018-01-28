package com.products.productmanagement.unit;

import com.products.productmanagement.entity.Product;
import com.products.productmanagement.exception.DuplicatedProductNameOnDatabaseException;
import com.products.productmanagement.exception.ProductNameAlreadyExistException;
import com.products.productmanagement.exception.ProductNotFoundException;
import com.products.productmanagement.repository.ProductRepository;
import com.products.productmanagement.service.ImageService;
import com.products.productmanagement.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ImageService imageService;

    @Test
    public void updateProductByNameHappyScenarioTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Product productFoundDB = new Product("TestProductName-2", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName-2")).thenReturn(new ArrayList<Product>() {{
            add(productFoundDB);
        }});
        Mockito.when(productRepository.save(productTest)).thenReturn(productTest);
        Product productReturn = productService.updateProductByName("TestProductName-2", productTest);
        assertEquals(productReturn.getProductName(), productTest.getProductName());
        assertEquals(productReturn.getProductDescription(), productTest.getProductDescription());
    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProductByNameWhenProductNotFoundTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        productService.updateProductByName("TestProductName", productTest);
    }

    @Test(expected = DuplicatedProductNameOnDatabaseException.class)
    public void updateProductTypeWhenImageExistOnDatabaseTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productTest);
            add(productTest);
        }});
        productService.updateProductByName("TestProductName", productTest);
    }

    @Test
    public void saveNewProductHappyScenarioTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.save(productTest)).thenReturn(productTest);
        Product productReturn = productService.saveNewProduct(productTest);
        assertEquals(productReturn.getProductName(), productTest.getProductName());
        assertEquals(productReturn.getProductDescription(), productTest.getProductDescription());
    }

    @Test(expected = ProductNameAlreadyExistException.class)
    public void saveNewProductTestWhenProductExistOnDatabaseTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productTest);
        }});
        Mockito.when(productRepository.save(productTest)).thenReturn(productTest);
        productService.saveNewProduct(productTest);
    }

    @Test
    public void findProductByNameHappyScenarioTest() {
        Product productFoundDB = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productFoundDB);
        }});
        Product product = productService.findProductByName("TestProductName");
        assertEquals(product.getProductName(), productFoundDB.getProductName());
        assertEquals(product.getProductDescription(), productFoundDB.getProductDescription());
    }

    @Test
    public void findProductByNameWhenNotFoundOnDatabaseTest() {
       Product product = productService.findProductByName("TestProductName");
        assertEquals(null, product);
    }

    @Test(expected = DuplicatedProductNameOnDatabaseException.class)
    public void findProductByNameWhenNameIsDuplicatedOnDatabaseTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productTest);
            add(productTest);
        }});
        productService.findProductByName("TestProductName");
    }

    @Test
    public void deleteProductHappyScenarioTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productTest);
        }});
        try {
            productService.deleteProductByName("TestProductName");
        } catch (Exception ex) {
            assertFalse(true);
        }
    }

    @Test(expected = ProductNotFoundException.class)
    public void deleteProductWhenProductNotExistOnDatabaseTest() {
        productService.deleteProductByName("TestProductName");
    }

    @Test(expected = DuplicatedProductNameOnDatabaseException.class)
    public void deleteProductWhenProductIsDuplicatedOnDatabaseTest() {
        Product productTest = new Product("TestProductName", "Product type test");
        Mockito.when(productRepository.findByProductName("TestProductName")).thenReturn(new ArrayList<Product>() {{
            add(productTest);
            add(productTest);
        }});
        productService.deleteProductByName("TestProductName");
    }

}
