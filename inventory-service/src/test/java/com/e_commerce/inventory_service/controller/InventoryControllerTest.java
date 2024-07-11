package com.e_commerce.inventory_service.controller;

import com.e_commerce.inventory_service.dto.InventoryResponse;
import com.e_commerce.inventory_service.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    void isInStock_ReturnsInventoryResponses() throws Exception {
        // Given
        when(inventoryService.isInStock(Arrays.asList("sku-001", "sku-002")))
                .thenReturn(Arrays.asList(
                        new InventoryResponse("sku-001", true),
                        new InventoryResponse("sku-002", false)
                ));

        // When/Then
        mockMvc.perform(get("/api/inventory")
                        .param("skuCode", "sku-001", "sku-002"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].skuCode").value("sku-001"))
                .andExpect(jsonPath("$[0].isInStock").value(true))
                .andExpect(jsonPath("$[1].skuCode").value("sku-002"))
                .andExpect(jsonPath("$[1].isInStock").value(false));
    }
}

