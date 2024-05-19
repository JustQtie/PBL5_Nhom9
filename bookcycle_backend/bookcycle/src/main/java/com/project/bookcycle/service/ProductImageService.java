package com.project.bookcycle.service;

import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService{

    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getThumbnailByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public void deleteProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }

}
