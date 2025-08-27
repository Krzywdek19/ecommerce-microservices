package com.krzywdek19.product_service.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(MeterRegistry meterRegistry) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats());

        cacheManager.setCacheNames(Arrays.asList(
                "products",
                "productsByCategory",
                "productsByPriceRange"));

        meterRegistry.config().commonTags("application", "product-service");

        return cacheManager;
    }

    @Bean
    public Cache productsCache(MeterRegistry meterRegistry) {
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache =
                Caffeine.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(500)
                        .recordStats()
                        .build();

        com.github.benmanes.caffeine.cache.stats.CacheStats stats = caffeineCache.stats();
        meterRegistry.gauge("cache.products.size",
                List.of(Tag.of("name", "products")),
                caffeineCache, cache -> cache.estimatedSize());

        return new CaffeineCache("products", caffeineCache);
    }

    @Bean
    public Cache productsByCategoryCache(MeterRegistry meterRegistry) {
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache =
                Caffeine.newBuilder()
                        .expireAfterWrite(15, TimeUnit.MINUTES)
                        .maximumSize(300)
                        .recordStats()
                        .build();

        meterRegistry.gauge("cache.productsByCategory.size",
                List.of(Tag.of("name", "productsByCategory")),
                caffeineCache, cache -> cache.estimatedSize());

        return new CaffeineCache("productsByCategory", caffeineCache);
    }
}