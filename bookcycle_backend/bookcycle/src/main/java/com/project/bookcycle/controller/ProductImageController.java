package com.project.bookcycle.controller;

import com.project.bookcycle.model.ProductImage;
import com.project.bookcycle.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
