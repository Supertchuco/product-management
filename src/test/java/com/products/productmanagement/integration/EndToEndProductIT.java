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

/*
    // 02 The API should return 200 Status Code when an user with multiple clients searches and select a client without Mobile URL using PII search
    @Test
    public void shouldReturn200StatusCodeWhenAnUserWithMultipleClientsSearchesAndSelectAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/UserWithMultipleClientsSearchesAndSelectAClientWithMobileURLUsingPIISearch.json");

        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "CONFIRA002");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("JSmith@CONFIRA002")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Direct")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Registered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("CONFIRA002")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("CONFIRA002")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // Not Registered User
    // 03 The API should return 200 Status Code when a not registered user finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenANotRegisteredUserFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ANotRegisteredUserFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Unregistered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // 04 The API should return 200 Status Code when a not registered user finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenANotRegisteredUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ANotRegisteredUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("UNREGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // Direct User With User ID
    // 05 The API should return 200 Status Code when a direct user with User ID finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenADirectUserWithUserIDFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ADirectUserWithUserIDFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");

        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("TTassinari@LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Direct")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Registered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // 06 The API should return 200 Status Code when a direct user with User ID finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenADirectUserWithUserIDFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ADirectUserWithUserIDFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("jb@qm16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Registered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // Federated User
    // 07 The API should return 200 Status Code when a federated user with User ID finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenAFederatedUserWithUserIDFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/AFederatedUserWithUserIDFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Federated")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href", equalToIgnoringCase("https://mobifed-dit.nj.adp.com/oauth/client/24hf")));
    }

    // 08 The API should return 200 Status Code when a federated user with User ID finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenAFederatedUserWithUserIDFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/AFederatedUserWithUserIDFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Federated")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Registered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // Dual User
    // 09 The API should return 200 Status Code when a dual user finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenADualUserFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ADualUserFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Dual")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("Registered")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LIZB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href", equalToIgnoringCase("https://mobifed-dit.nj.adp.com/oauth/client/24hf")));
    }

    // 10 The API should return 200 Status Code when a dual user finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenADualUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ADualUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("itestone1@qm16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // Suspended Users
    // 11 The API should return 200 Status Code when a suspended direct user finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenASuspendedDirectUserFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ASuspendedDirectUserFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("ssodemoditone")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Direct")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // 12 The API should return 200 Status Code when a suspended dual user finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenASuspendedDualUserFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ASuspendedDualUserFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Dual")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href", equalToIgnoringCase("https://mobifed-dit.nj.adp.com/oauth/client/24hf")));
    }

    // 13 The API should return 200 Status Code when a suspended federated user finds and selects a client with Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenASuspendedFederatedUserFindsAndSelectsAClientWithMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ASuspendedFederatedUserFindsAndSelectsAClientWithMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID").doesNotExist())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Federated")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href", equalToIgnoringCase("https://mobifed-dit.nj.adp.com/oauth/client/24hf")));
    }

    // 14 The API should return 200 Status Code when a suspended direct user finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenASuspendedDirectUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ASuspendedDirectUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("chent")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Direct")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // 15 The API should return 200 Status Code when a suspended dual user finds and selects a client without Mobile URL using PII search.
    @Test
    public void shouldReturn200StatusCodeWhenASuspendedDualUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch() throws Exception {
        String payload = readJSON("request/200StatusCode/ASuspendedDualUserFindsAndSelectsAClientWithoutMobileURLUsingPIISearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.userAccount.userID", equalToIgnoringCase("chent")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountTypeCode.codeValue", equalToIgnoringCase("Direct")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.accountStatusCode.codeValue", equalToIgnoringCase("REGISTERED")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href").doesNotExist());
    }

    // ORC Search
    // The API should return 200 Status Code when an user finds and selects a client with Mobile URL using ORC search.
    @Test
    public void shouldReturn200StatusCodeWhenUserFindsAndSelectsAClientWithMobileURLUsingORCSearch() throws Exception {
        String payload = readJSON("request/200StatusCode/UserFindsAndSelectsAClientWithMobileURLUsingORCSearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("ORC")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LIZB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].links[0].href", equalToIgnoringCase("https://mobifed-dit.nj.adp.com/oauth/client/24hf")));
    }

    // The API should return 200 Status Code when an user finds and selects a client without Mobile URL using ORC search.
    @Test
    public void shouldReturn200StatusCodeWhenUserFindsAndSelectsAClientWithoutMobileURLUsingORCSearch() throws Exception {
        String payload = readJSON("request/200StatusCode/UserFindsAndSelectsAClientWithoutMobileURLUsingORCSearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("ORC")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();
        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "QM16");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("QM16")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("QM16")));
    }

    // The API should return 200 Status Code when an user finds a not federated client using ORC Search
    @Test
    public void shouldReturn200StatusCodeWhenAnUserFindsANotFederatedClientUsingORCSearch() throws Exception {
        String payload = readJSON("request/200StatusCode/AnUserFindsANotFederatedClientUsingORCSearch.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("ORC")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "DitVeDemo");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("DitVeDemo")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("DitVeDemo")));
    }

    // The API should return 200 Status Code when an user contains first name and last name with accents
    @Test
    public void shouldReturn200StatusCodeWhenAnUserWithFirstNameAndLastNameContainingAccents() throws Exception {
        String payload = readJSON("request/200StatusCode/AnUserWithFirstNameAndLastNameContainingAccents.json");
        MvcResult result = this.mockMvc
                .perform(post(requestEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountAccessInfo.request")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.eventContext.requestTypeCode.codeValue", equalToIgnoringCase("PII")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts").isArray())
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].accountToken", is(notNullValue())))
                .andReturn();

        String token = extractAccountTokenValue(result.getResponse().getContentAsString(), "NowCorp");

        this.mockMvc
                .perform(post(decryptEndpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insertTokenInPayload(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events[0].eventNameCode.codeValue", equalToIgnoringCase("accountInfo.decrypt")))
                .andExpect(jsonPath("$.events[0].eventStatusCode.codeValue", equalToIgnoringCase("complete")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0]", is(notNullValue())))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerID", equalToIgnoringCase("LizB2")))
                .andExpect(jsonPath("$.events[0].data.output.accountAccessInfo.organizationAccounts[0].providerName", equalToIgnoringCase("NowCorp")));
    }*/
}

