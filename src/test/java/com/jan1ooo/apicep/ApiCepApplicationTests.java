package com.jan1ooo.apicep;

import com.jan1ooo.apicep.model.Address;
import com.jan1ooo.apicep.service.CorreiosService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompare;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@MockServerTest({"correios.base.url=http://localhost:${mockServerPort}/ceps.csv"})
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiCepApplicationTests {
    @Autowired private MockMvc mockMvc;
    @Autowired private CorreiosService service;
    private MockServerClient mockServerClient;

    @Test
    @Order(1)
    public void testGetZipCodeWhenNotReady() throws Exception {
        mockMvc.perform(get("/zipcode/02950000")).andExpect(status().is(503));
    }

    @Test
    @Order(2)
    public void testSetup() throws Exception {
        String csvContent = "SP,Sao Paulo,Vila Pereira Barreto,02950000,Avenida Miguel de Castro,,,,,,,,,,";

        //
        // Mock SETUP Endpoint
        mockServerClient
                .when(request()
                        .withMethod("GET")
                        .withPath("/ceps.csv"))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withContentType(org.mockserver.model.MediaType.PLAIN_TEXT_UTF_8)
                        .withBody(csvContent)
                );

        //
        // Setup it
        this.service.setup();
    }

    @Test
    @Order(3)
    void testGetZipCodeThatDoesntExist() throws Exception {
        mockMvc.perform(get("/zipcode/99999999")).andExpect(status().is(204));
    }

    @Test
    @Order(4)
    void tesGetCorrectZipCode() throws Exception {
        MvcResult result = mockMvc.perform(get("/zipcode/02950000"))
                .andExpect(status().is(200))
                .andReturn();

        String addressResultJson = result.getResponse().getContentAsString();
        String addressCorrectJson = new ObjectMapper().writeValueAsString(
                Address.builder()
                        .zipcode("02950000")
                        .street("Avenida Miguel de Castro")
                        .city("Sao Paulo")
                        .state("SP")
                        .district("Vila Pereira Barreto").build());
        //
        //
        JSONAssert.assertEquals(addressCorrectJson, addressResultJson, false);
    }
}

