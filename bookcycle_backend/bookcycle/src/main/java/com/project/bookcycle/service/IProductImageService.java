package com.project.bookcycle.service;

import com.project.bookcycle.model.ProductImage;

import java.util.List;

public interface IProductImageService {
    List<ProductImage> getThumbnailByProductId(Long productId);
}
