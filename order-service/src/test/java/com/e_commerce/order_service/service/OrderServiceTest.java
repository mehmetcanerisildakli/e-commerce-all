package com.e_commerce.order_service.service;

import com.e_commerce.order_service.dto.InventoryResponse;
import com.e_commerce.order_service.dto.OrderLineItemsDto;
import com.e_commerce.order_service.dto.OrderRequest;
import com.e_commerce.order_service.model.Order;
import com.e_commerce.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private WebClient webClient;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        // Initialize orderRequest with test data
        orderRequest = new OrderRequest();
        OrderLineItemsDto itemDto = new OrderLineItemsDto();
        itemDto.setSkuCode("test-sku");
        itemDto.setPrice(BigDecimal.valueOf(100));
        itemDto.setQuantity(1);
        orderRequest.setOrderLineItemsDtoList(Arrays.asList(itemDto));
    }

    @Test
    void placeOrder_AllProductsInStock_OrderPlacedSuccessfully() {
        // Given
        InventoryResponse[] inventoryResponses = new InventoryResponse[]{
                new InventoryResponse("test-sku", true)
        };

        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(anyString(), any(Function.class))).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));


        // When
        orderService.placeOrder(orderRequest);

        // Then
        verify(orderRepository, times(1)).save(any(Order.class));
    }


    @Test
    void placeOrder_ProductNotInStock_ThrowsException() {
        // Given
        InventoryResponse[] inventoryResponses = new InventoryResponse[] {
                new InventoryResponse("test-sku", false)
        };

        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(anyString(), any(Function.class))).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(inventoryResponses));

        // When/Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(orderRequest));
        assertEquals("Product is not in stock, please try later", exception.getMessage());

        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void placeOrder_WebClientThrowsException_ThrowsException() {
        // Given
        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(anyString(), any(Function.class))).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InventoryResponse[].class)).thenThrow(WebClientResponseException.class);

        // When/Then
        assertThrows(WebClientResponseException.class, () -> orderService.placeOrder(orderRequest));
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}
