package com.project.bookcycle.controller;

import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.response.ProductImageResponse;
import com.project.bookcycle.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/productimages")
@RequiredArgsConstructor
public class ProductImageController {
    private final ProductImageService productImageService;

    @GetMapping("thumbnails/{id}")
    public ResponseEntity<?> getThumbnail(
            @PathVariable Long id
    ){
        try {
            List<ProductImage> productImageList = productImageService.getThumbnailByProductId(id);
            ArrayList<String> listThumbnails = new ArrayList<>();
            for(ProductImage productImage : productImageList){
                listThumbnails.add(productImage.getImageUrl());
            }
            return ResponseEntity.ok().body(listThumbnails);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("thumbnails/{id}")
    public ResponseEntity<?> deleteProductImage(
            @PathVariable Long id
    ){
        try {
            productImageService.deleteProductId(id);
            return ResponseEntity.ok().body(ProductImageResponse.builder().ec("0").build());
        }catch (Exception e){
            return ResponseEntity.ok().body(ProductImageResponse.builder().ec("-1").message(e.getMessage()).build());
        }
    }
}
