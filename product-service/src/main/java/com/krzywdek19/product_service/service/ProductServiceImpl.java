package com.krzywdek19.product_service.service;

import com.krzywdek19.product_service.model.Category;
import com.krzywdek19.product_service.model.Product;
import com.krzywdek19.product_service.mapper.ProductMapper;
import com.krzywdek19.product_service.repository.ProductRepository;
import com.krzywdek19.product_service.dto.ProductCreateDTO;
import com.krzywdek19.product_service.dto.ProductResponseDTO;
import com.krzywdek19.product_service.dto.ProductUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Product createProduct(ProductCreateDTO product) {
        Product newProduct = productMapper.dtoToProduct(product);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductUpdateDTO product) {
        Product existingProduct = findProductById(id);

        updateIfNotNull(product.getName(), existingProduct::setName);
        updateIfNotNull(product.getDescription(), existingProduct::setDescription);
        updateIfNotNull(product.getPrice(), existingProduct::setPrice);
        updateIfNotNull(product.getCategory(), existingProduct::setCategory);
        updateIfNotNull(product.getSkuCode(), existingProduct::setSkuCode);
        updateIfNotNull(product.getQuantity(), existingProduct::setQuantity);

        return productRepository.save(existingProduct);
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    @Override
    public ProductResponseDTO getProduct(Long id) {
        return productMapper.productToDto(findProductById(id));
    }

    @Override
    public List<ProductResponseDTO> getProducts() {
        return mapToProductResponseDTOList(productRepository.findAll());
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(Category category) {
        return mapToProductResponseDTOList(productRepository.findByCategory(category));
    }

    @Override
    public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return mapToProductResponseDTOList(productRepository.findByPriceBetween(minPrice, maxPrice));
    }

    @Override
    public boolean isProductInStock(Long id) {
        return productRepository.findById(id)
                .map(product -> product.getQuantity() > 0)
                .orElse(false);
    }

    @Override
    public boolean reduceProductStock(Long id, Integer quantity) {
        Product product = findProductById(id);

        if (product.getQuantity() < quantity) {
            return false;
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        return true;
    }

    @Override
    public List<ProductResponseDTO> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getProducts();
        }

        String searchTerm = "%" + keyword.toLowerCase() + "%";
        return mapToProductResponseDTOList(
                productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        searchTerm, searchTerm)
        );
    }

    @Override
    public List<ProductResponseDTO> getRecentlyAddedProducts(int limit) {
        return productRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkProductAvailability(List<Long> productIds) {
        List<Product> products = getProductsByIdsWithValidation(productIds);

        if (products.isEmpty()) {
            return true;
        }

        long availableCount = products.stream()
                .filter(product -> product.getQuantity() > 0)
                .count();

        return availableCount == productIds.size();
    }

    @Override
    public List<Product> getProductsByIds(List<Long> productIds) {
        return getProductsByIdsWithValidation(productIds);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " has not been found"));
    }

    private List<ProductResponseDTO> mapToProductResponseDTOList(List<Product> products) {
        return products.stream()
                .map(productMapper::productToDto)
                .collect(Collectors.toList());
    }

    private List<Product> getProductsByIdsWithValidation(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        return productRepository.findAllById(productIds);
    }
}