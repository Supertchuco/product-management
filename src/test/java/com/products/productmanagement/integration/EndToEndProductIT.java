package com.products.productmanagement.integration;

import com.products.productmanagement.controller.ProductController;
import com.products.productmanagement.entity.Product;
import com.products.productmanagement.exception.ProductNameAlreadyExistException;
import com.products.productmanagement.repository.ProductRepository;
import com.products.productmanagement.service.ProductService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EndToEndProductIT {

    private static final String requestEndpoint = "http://localhost:8090/productManagement/api/product";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static String readJSON(String filename) {
        try {
            return FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + filename), "UTF-8");
        } catch (IOException exception) {
            return null;
        }
    }

    @Test
    public void shouldReturn200WhenProductInsertWithoutImage(){
        String payload = readJSON("request/product/productInsertWithoutImage200StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint +  "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn200WhenProductInsertWithImage(){
        String payload = readJSON("request/product/productInsertWithImage200StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint +  "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn200WhenDeleteProductInDatabase(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/deleteByName/Tang", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Product deleted"));
    }

    @Test
    public void shouldReturn200WhenEditProductNameInDatabase() throws Exception {
        String payload = readJSON("request/product/productInsertWithoutImage200StatusAndExistInDatabaseResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(requestEndpoint +  "/editByName/Golly", HttpMethod.PATCH, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assertEquals(true, StringUtils.contains(response.toString(), "photo"));

    }

    @Test
    public void shouldReturn400WhenProductNameAlreadyExistInDatabase() throws Exception {
        String payload = readJSON("request/product/productInsertWithoutImage200StatusAndExistInDatabaseResponseRequest.json");
    }

}

