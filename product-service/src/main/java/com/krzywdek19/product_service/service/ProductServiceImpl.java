package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.dto.ProductStockResponse;
import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.model.Product;
import com.krzywdek19.product_service.mapper.ProductMapper;
import com.krzywdek19.product_service.repository.ProductRepository;
import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;
import com.krzywdek19.product_service.exception.ProductNotFoundException;
import com.krzywdek19.product_service.exception.InsufficientStockException;
import com.krzywdek19.product_service.exception.InvalidProductDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    @CachePut(value = "products", key = "#result.id")
    public ProductResponseDTO createProduct(ProductCreateDTO product) {
        validateNotNull(product, "Product data cannot be null");

        Product newProduct = productMapper.dtoToProduct(product);
        Product savedProduct = productRepository.save(newProduct);
        return productMapper.productToDto(savedProduct);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#id")
    public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO product) {
        validateNotNull(product, "Update data cannot be null");

        Product existingProduct = findProductById(id);
        updateProductFields(existingProduct, product);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.productToDto(updatedProduct);
    }

    private void updateProductFields(Product product, ProductUpdateDTO updateDTO) {
        updateIfNotNull(updateDTO.getName(), product::setName);
        updateIfNotNull(updateDTO.getDescription(), product::setDescription);
        updateIfNotNull(updateDTO.getPrice(), product::setPrice);
        updateIfNotNull(updateDTO.getCategory(), product::setCategory);
        updateIfNotNull(updateDTO.getSkuCode(), product::setSkuCode);
        updateIfNotNull(updateDTO.getQuantity(), product::setQuantity);
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO getProduct(Long id) {
        return productMapper.productToDto(findProductById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::productToDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(Category category, int page, int size) {
        validateNotNull(category, "Category cannot be null");
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        return productPage.map(productMapper::productToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        validateNotNull(minPrice, "Minimum price cannot be null");
        validateNotNull(maxPrice, "Maximum price cannot be null");

        if (minPrice.compareTo(maxPrice) > 0) {
            throw new InvalidProductDataException("Minimum price cannot be greater than maximum price");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        return productPage.map(productMapper::productToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInStock(Long id) {
        return findProductById(id).getQuantity() > 0;
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#id")
    public boolean reduceProductStock(Long id, Integer quantity) {
        validatePositive(quantity, "Quantity must be a positive number");

        Product product = findProductById(id);
        validateSufficientStock(id, quantity, product.getQuantity());

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        return true;
    }

    private void validateSufficientStock(Long productId, int requested, int available) {
        if (available < requested) {
            throw new InsufficientStockException(productId, requested, available);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll(pageable).map(productMapper::productToDto);
        }

        String searchTerm = "%" + keyword.toLowerCase() + "%";
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
        return productPage.map(productMapper::productToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getRecentlyAddedProducts(int limit) {
        validatePositive(limit, "Limit must be a positive number");

        return productRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByIds(List<Long> productIds) {
        validateNotNull(productIds, "Product IDs list cannot be null");

        if (productIds.isEmpty()) {
            return List.of();
        }

        List<Product> products = getProductsWithValidation(productIds);
        return mapToProductResponseDTOList(products);
    }

    @Override
    public List<ProductStockResponse> checkProductsAvailability(List<Long> productIds) {
        return productIds.stream()
                .map(id -> {
                    var product = productRepository.findById(id)
                            .orElse(null);

                    if (product == null) {
                        return ProductStockResponse.builder()
                                .productId(id)
                                .inStock(false)
                                .quantity(0)
                                .build();
                    }

                    return ProductStockResponse.builder()
                            .productId(id)
                            .inStock(product.getQuantity() > 0)
                            .quantity(product.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<Product> getProductsWithValidation(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("One or more requested products not found");
        }
        return products;
    }

    private Product findProductById(Long id) {
        validateNotNull(id, "Product ID cannot be null");
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private List<ProductResponseDTO> mapToProductResponseDTOList(List<Product> products) {
        return products.stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

    private <T> void validateNotNull(T value, String message) {
        if (value == null) {
            throw new InvalidProductDataException(message);
        }
    }

    private void validatePositive(Integer value, String message) {
        validateNotNull(value, message);
        if (value <= 0) {
            throw new InvalidProductDataException(message);
        }
    }
}