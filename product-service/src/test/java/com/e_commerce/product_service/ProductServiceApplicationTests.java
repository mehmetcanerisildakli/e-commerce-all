package com.e_commerce.product_service;

import com.e_commerce.product_service.dto.ProductRequest;
import com.e_commerce.product_service.dto.ProductResponse;
import com.e_commerce.product_service.repository.ProductRepository;
import com.e_commerce.product_service.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    private List<ProductResponse> productResponses;

    @MockBean
    private ProductService productService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void when_sendRequestForANewProduct_then_serviceShouldCreateProduct() throws Exception {

        // Given
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());

        //Then
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void when_sendRequestToGetProducts_then_allProductsShouldBeGet() throws Exception {

        // Given
        setUp();

        // When
        when(productService.getAllProducts()).thenReturn(productResponses);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("12345"))
                .andExpect(jsonPath("$[0].name").value("Sample Product 1"))
                .andExpect(jsonPath("$[0].description").value("This is a sample product 1."))
                .andExpect(jsonPath("$[0].price").value(19.99))
                .andExpect(jsonPath("$[1].id").value("67890"))
                .andExpect(jsonPath("$[1].name").value("Sample Product 2"))
                .andExpect(jsonPath("$[1].description").value("This is a sample product 2."))
                .andExpect(jsonPath("$[1].price").value(29.99));

    }

    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ProductResponse productResponse1 = new ProductResponse("12345", "Sample Product 1", "This is a sample product 1.", BigDecimal.valueOf(19.99));
        ProductResponse productResponse2 = new ProductResponse("67890", "Sample Product 2", "This is a sample product 2.", BigDecimal.valueOf(29.99));
        productResponses = Arrays.asList(productResponse1, productResponse2);
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iPhone")
                .description("iPhone 25")
                .price(BigDecimal.valueOf(1200))
                .build();
    }

}
