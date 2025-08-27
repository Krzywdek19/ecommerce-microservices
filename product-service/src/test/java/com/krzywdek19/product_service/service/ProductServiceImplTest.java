package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;
import com.krzywdek19.product_service.exception.InsufficientStockException;
import com.krzywdek19.product_service.exception.InvalidProductDataException;
import com.krzywdek19.product_service.exception.ProductNotFoundException;
import com.krzywdek19.product_service.mapper.ProductMapper;
import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.model.Product;
import com.krzywdek19.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private ProductResponseDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(BigDecimal.valueOf(100));
        testProduct.setCategory(Category.SHIRTS);
        testProduct.setSkuCode("SKU-123");
        testProduct.setQuantity(10);
        testProduct.setCreatedAt(LocalDateTime.now());
        testProduct.setUpdatedAt(LocalDateTime.now());

        testProductDTO = new ProductResponseDTO();
        testProductDTO.setId(1L);
        testProductDTO.setName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(BigDecimal.valueOf(100));
        testProductDTO.setCategory(Category.SHIRTS);
        testProductDTO.setSkuCode("SKU-123");
        testProductDTO.setQuantity(10);
    }

    @Test
    void createProduct_ValidInput_ReturnsCreatedProduct() {
        // Arrange
        ProductCreateDTO createDTO = new ProductCreateDTO();
        createDTO.setName("Test Product");

        when(productMapper.dtoToProduct(any(ProductCreateDTO.class))).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(productMapper.productToDto(any(Product.class))).thenReturn(testProductDTO);

        // Act
        ProductResponseDTO result = productService.createProduct(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testProductDTO.getId(), result.getId());
        assertEquals(testProductDTO.getName(), result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_NullInput_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidProductDataException.class, () ->
                productService.createProduct(null)
        );
    }

    @Test
    void getProduct_ExistingId_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        ProductResponseDTO result = productService.getProduct(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void getProduct_WhenCached_DoesNotCallRepository() {
        // Arrange - First call will save product to cache
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act - First call
        productService.getProduct(1L);

        // Second call - should use cache instead of repo
        productService.getProduct(1L);

        // Assert - repository should be called only once
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProduct_NonExistingId_ThrowsException() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () ->
                productService.getProduct(99L)
        );
    }

    @Test
    void getProducts_WithPagination_ReturnsPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(testProduct));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        Page<ProductResponseDTO> result = productService.getProducts(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void updateProduct_ValidInput_ReturnsUpdatedProduct() {
        // Arrange
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setPrice(BigDecimal.valueOf(200));

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Name");
        updatedProduct.setDescription("Test Description");
        updatedProduct.setPrice(BigDecimal.valueOf(200));
        updatedProduct.setCategory(Category.SHIRTS);
        updatedProduct.setQuantity(10);

        ProductResponseDTO updatedDTO = new ProductResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated Name");
        updatedDTO.setPrice(BigDecimal.valueOf(200));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.productToDto(any(Product.class))).thenReturn(updatedDTO);

        // Act
        ProductResponseDTO result = productService.updateProduct(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals(0, BigDecimal.valueOf(200).compareTo(result.getPrice()));
    }

    @Test
    void deleteProduct_ExistingId_DeletesProduct() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_NonExistingId_ThrowsException() {
        // Arrange
        when(productRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () ->
                productService.deleteProduct(99L)
        );
    }

    @Test
    void reduceProductStock_SufficientStock_ReducesStock() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        boolean result = productService.reduceProductStock(1L, 5);

        // Assert
        assertTrue(result);
        assertEquals(5, testProduct.getQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void reduceProductStock_InsufficientStock_ThrowsException() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () ->
                productService.reduceProductStock(1L, 20)
        );
    }

    @Test
    void getProductsByCategory_WithPagination_ReturnsPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(testProduct));

        when(productRepository.findByCategory(Category.SHIRTS, pageable)).thenReturn(productPage);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        Page<ProductResponseDTO> result = productService.getProductsByCategory(Category.SHIRTS, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getProductsByPriceRange_WithPagination_ReturnsPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(testProduct));

        when(productRepository.findByPriceBetween(
                BigDecimal.valueOf(50), BigDecimal.valueOf(150), pageable))
                .thenReturn(productPage);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        Page<ProductResponseDTO> result = productService.getProductsByPriceRange(
                BigDecimal.valueOf(50), BigDecimal.valueOf(150), 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getProductsByIds_ExistingIds_ReturnsProducts() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Second Product");

        ProductResponseDTO dto2 = new ProductResponseDTO();
        dto2.setId(2L);
        dto2.setName("Second Product");

        List<Product> products = Arrays.asList(testProduct, product2);

        when(productRepository.findAllById(ids)).thenReturn(products);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);
        when(productMapper.productToDto(product2)).thenReturn(dto2);

        // Act
        List<ProductResponseDTO> result = productService.getProductsByIds(ids);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void checkProductAvailability_AllAvailable_ReturnsTrue() {
        // Arrange
        List<Long> ids = Collections.singletonList(1L);
        List<Product> products = Collections.singletonList(testProduct);

        when(productRepository.findAllById(ids)).thenReturn(products);

        // Act
        boolean result = productService.checkProductAvailability(ids);

        // Assert
        assertTrue(result);
    }

    @Test
    void searchProducts_WithPagination_ReturnsPagedResults() {
        // Arrange
        String keyword = "test";
        String searchTerm = "%test%";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(testProduct));

        when(productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm, pageable))
                .thenReturn(productPage);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        Page<ProductResponseDTO> result = productService.searchProducts(keyword, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testProductDTO.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getRecentlyAddedProducts_ReturnsLimitedProducts() {
        // Arrange
        List<Product> recentProducts = Collections.singletonList(testProduct);
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(recentProducts);
        when(productMapper.productToDto(testProduct)).thenReturn(testProductDTO);

        // Act
        List<ProductResponseDTO> result = productService.getRecentlyAddedProducts(5);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}