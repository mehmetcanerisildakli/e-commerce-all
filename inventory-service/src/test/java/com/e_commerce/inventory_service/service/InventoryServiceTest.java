package com.e_commerce.inventory_service.service;
import com.e_commerce.inventory_service.dto.InventoryResponse;
import com.e_commerce.inventory_service.model.Inventory;
import com.e_commerce.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        // Setup mock data
        Inventory inventory1 = new Inventory(1L,"sku-001", 10); // In stock
        Inventory inventory2 = new Inventory(2L,"sku-002", 0);  // Out of stock

        when(inventoryRepository.findBySkuCodeIn(Arrays.asList("sku-001", "sku-002")))
                .thenReturn(Arrays.asList(inventory1, inventory2));
    }

    @Test
    void isInStock_ReturnsCorrectInventoryResponse() {
        // Given
        List<String> skuCodes = Arrays.asList("sku-001", "sku-002");

        // When
        List<InventoryResponse> responses = inventoryService.isInStock(skuCodes);

        // Then
        assertEquals(2, responses.size());

        // Check for sku-001
        InventoryResponse response1 = responses.get(0);
        assertEquals("sku-001", response1.getSkuCode());
        assertEquals(true, response1.getIsInStock());

        // Check for sku-002
        InventoryResponse response2 = responses.get(1);
        assertEquals("sku-002", response2.getSkuCode());
        assertEquals(false, response2.getIsInStock());
    }
}
