package com.project.bookcycle.service;

import com.project.bookcycle.dto.ProductDTO;
import com.project.bookcycle.dto.ProductImageDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.exceptions.InvalidParamException;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.response.ProductResponse;

import java.util.List;


public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProduct(long id) throws DataNotFoundException;
    List<ProductResponse> getAllProducts(String keyword, Long categoryId);
    List<ProductResponse> getProductByUserId(Long userId);
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existsByName(String name);
    void updateThumbnail(long id, String thumbnailUrl);
    ProductImage createProductImage(
            long productId,
            ProductImageDTO productImageDTO
    ) throws DataNotFoundException, InvalidParamException;
}
