package com.project.bookcycle.service;

import com.project.bookcycle.dto.ProductDTO;
import com.project.bookcycle.dto.ProductImageDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.exceptions.InvalidParamException;
import com.project.bookcycle.model.Category;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.model.User;
import com.project.bookcycle.repository.CategoryRepository;
import com.project.bookcycle.repository.ProductImageRepository;
import com.project.bookcycle.repository.ProductRepository;
import com.project.bookcycle.repository.UserRepository;
import com.project.bookcycle.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find category with id: "+productDTO.getCategoryId()));
        User existingUser = userRepository
                .findById(productDTO.getUserId())
                .orElseThrow(()->
                        new DataNotFoundException("Cannot find user with id: "+productDTO.getUserId()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .author(productDTO.getAuthor())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .status(productDTO.getStatus())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .user(existingUser)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProduct(long id) throws DataNotFoundException {
        return productRepository.findById(id).
                orElseThrow(()-> new DataNotFoundException(
                        "Cannot find product with id ="+id));
    }

    @Override
    public List<ProductResponse> getAllProducts(String keyword, Long categoryId) {
        return productRepository
                .searchProducts(keyword, categoryId)
                .stream()
                .map(ProductResponse::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getListProduct() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductByUserId(Long userId) {
        return productRepository.findByUserId(userId)
                .stream()
                .map(ProductResponse::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductNotUserId(Long userId) {
        return productRepository.findAllByUserIdNot(userId)
                .stream()
                .map(ProductResponse::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProduct(id);
        if(existingProduct != null) {
            //copy các thuộc tính từ DTO -> Product
            //Có thể sử dụng ModelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: "+productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setAuthor(productDTO.getAuthor());
            existingProduct.setCategory(existingCategory);
            existingProduct.setStatus(productDTO.getStatus());
            existingProduct.setQuantity(productDTO.getQuantity());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public void updateThumbnail(long id, String thumbnailUrl) {
        productRepository.updateThumbnail(id, thumbnailUrl);
    }

    @Override
    public ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find product with id: "+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Ko cho insert quá 5 ảnh cho 1 sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException(
                    "Number of images must be <= "
                            +ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }

}
