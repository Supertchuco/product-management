package com.products.productmanagement.integration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


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
    public void shouldReturn200WhenInsertProductWithoutImage() {
        String payload = readJSON("request/product/productInsertWithoutImage200StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn200WhenInsertProductWithImage() {
        String payload = readJSON("request/product/productInsertWithImage200StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn200WhenInsertProductWithParentProduct() {
        String payload = readJSON("request/product/productInsertWithParentProduct200StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenInsertProductWithParentProductNotFound() {
        String payload = readJSON("request/product/productInsertWithParentProductNotFound400StatusResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Unable to find parent product with name Fanta Uva"));
    }

    @Test
    public void shouldReturn200WhenDeleteProductInDatabase() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/deleteByName/Tang", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Product deleted"));
    }

    @Test
    public void shouldReturn200WhenEditProductInDatabase() {
        String payload = readJSON("request/product/productInsertWithoutImage200StatusAndExistInDatabaseResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(requestEndpoint + "/editByName/Golly", HttpMethod.PATCH, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturn400WhenProductNameAlreadyExistInDatabaseInSaveOperation() {
        String payload = readJSON("request/product/productInsertWithoutImage200StatusAndExistInDatabaseResponseRequest.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(payload, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(requestEndpoint + "/save", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Product with this name [Tecate] already exist"));
    }

    @Test
    public void shouldReturn200WhenProductNotFoundInDatabaseInFindByNameOperation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(requestEndpoint + "/findByName/Sukest", HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, !StringUtils.contains(response.toString(), "Sukest"));
    }

    @Test
    public void shouldReturn200WhenProductFoundInDatabaseInFindByNameOperation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(requestEndpoint + "/findByName/Glub", HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Glub"));
    }

    @Test
    public void shouldReturn200WhenProductFoundInDatabaseInFindAllOperation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(requestEndpoint + "/findAll", HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, StringUtils.contains(response.toString(), "Tecate"));
        assertEquals(true, StringUtils.contains(response.toString(), "Fresh"));
        assertEquals(true, StringUtils.contains(response.toString(), "Glub"));
    }

}

