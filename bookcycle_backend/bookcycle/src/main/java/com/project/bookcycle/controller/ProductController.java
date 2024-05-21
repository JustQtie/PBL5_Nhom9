package com.project.bookcycle.controller;

import com.project.bookcycle.dto.ProductDTO;
import com.project.bookcycle.dto.ProductImageDTO;
import com.project.bookcycle.model.Product;
import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.response.ProductImageResponse;
import com.project.bookcycle.response.ProductListResponse;
import com.project.bookcycle.response.ProductResponse;
import com.project.bookcycle.service.IProductImageService;
import com.project.bookcycle.service.IProductService;
import com.project.bookcycle.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    private final IProductImageService productImageService;
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try {
            Product existingProduct = productService.getProduct(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(MessageKeys.UPLOAD_IMAGES_MAX_5);
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body(MessageKeys.UPLOAD_IMAGES_FILE_LARGE);
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE);
                }
                // Lưu file và cập nhật thumbnail
                String filename = storeFile(file);
                //lưu vào đối tượng product trong DB
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            String imgUrl = productImages.get(0).getImageUrl();
            productService.updateThumbnail(productId, imgUrl);
            ProductImageResponse productImageResponse = ProductImageResponse.builder().ec("0").build();
            return ResponseEntity.ok().body(productImageResponse);
        } catch (Exception e) {
            ProductImageResponse productImageResponse = ProductImageResponse.builder().ec("-1").message(e.getMessage()).build();
            return ResponseEntity.badRequest().body(productImageResponse);
        }
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/thumbnails/{id}")
    public ResponseEntity<?> deleteImage(
            @PathVariable Long id
    ){
        try {
            List<ProductImage> productImageList = productImageService.getThumbnailByProductId(id);
            ArrayList<String> listThumbnails = new ArrayList<>();
            for(ProductImage productImage : productImageList){
                listThumbnails.add(productImage.getImageUrl());
            }

            deleteImages(listThumbnails);

            return ResponseEntity.ok().body("Deleted thumbnails");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void deleteImages(List<String> imagePaths) {
        for (String imagePath : imagePaths) {
            try {
                Path path = Paths.get("uploads", imagePath);
                if (Files.exists(path)) {
                    Files.delete(path);
                } else {
                    System.out.println("File not found: " + path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(value = "keyword", defaultValue = "")  String keyword,
            @RequestParam(value = "category_id", defaultValue = "0")  Long categoryId
    ) {
        try {
            List<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId);
            return ResponseEntity.ok(ProductListResponse
                    .builder()
                    .productResponseList(productPage)
                    .ec("0")
                    .build());
        }catch(Exception e){
            return ResponseEntity.ok(ProductListResponse
                    .builder()
                    .ec("-1")
                    .build());
        }
    }
    @PostMapping("/get_list")
    public ResponseEntity<ProductListResponse> getListProduct(){
        try {
            List<ProductResponse> productPage = productService.getListProduct();
            return ResponseEntity.ok(ProductListResponse
                    .builder()
                    .productResponseList(productPage)
                    .ec("0")
                    .build());
        }catch(Exception e){
            return ResponseEntity.ok(ProductListResponse
                    .builder()
                    .ec("-1")
                    .build());
        }
    }

    @PostMapping("/byuser/{id}")
    public ResponseEntity<ProductListResponse> getProductsByUserId(
            @PathVariable("id") Long userId
    ){
        try {
            List<ProductResponse> productResponses = productService.getProductByUserId(userId);
            return ResponseEntity.ok(ProductListResponse.builder()
                        .productResponseList(productResponses)
                        .ec("0").build());
        }catch (Exception e){

            return ResponseEntity.badRequest().body(ProductListResponse.builder()
                    .ec("-1").build());
        }
    }

    @PostMapping("/bynotuser/{id}")
    public ResponseEntity<ProductListResponse> getProductsNotUserId(
            @PathVariable("id") Long userId
    ){
        try {
            List<ProductResponse> productResponses = productService.getProductNotUserId(userId);
            return ResponseEntity.ok(ProductListResponse.builder()
                    .productResponseList(productResponses)
                    .ec("0").build());
        }catch (Exception e){

            return ResponseEntity.badRequest().body(ProductListResponse.builder()
                    .ec("-1").build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long productId
    ) {
        try {
            Product existingProduct = productService.getProduct(productId);
            ProductResponse productResponse = ProductResponse.convertToProductResponse(existingProduct);
            productResponse.setEc("0");
            return ResponseEntity.ok(productResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ProductResponse.builder().ec("-1").build());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete success!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}