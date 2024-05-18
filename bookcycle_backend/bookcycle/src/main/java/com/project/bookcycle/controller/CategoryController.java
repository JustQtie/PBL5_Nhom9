package com.project.bookcycle.controller;

import com.project.bookcycle.dto.CategoryDTO;
import com.project.bookcycle.model.Category;
import com.project.bookcycle.response.CategoryResponse;
import com.project.bookcycle.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("")
    //Nếu tham số truyền vào là một object thì sao ? => Data Transfer object = Request object.
    public ResponseEntity<?> createCategory( //Dấu chấm hỏi trong tham số của phương thức ngụ ý nhận được nhiều kiểu trả vê.
                                             @Valid @RequestBody CategoryDTO categoryDTO, // @Valid: Khi một đối tượng được chuyển vào phương thức controller (CategoryDTO trong trường hợp này), Spring sẽ thực hiện kiểm tra hợp lệ trên đối tượng đó dựa trên các quy tắc đã được định nghĩa trong lớp đó.
                                             BindingResult result // BindingResult: Là một đối tượng được String lưu trữ các trường hợp lỗi xảy ra trong quá trình ánh xạ từ yêu cầu HTTP vào các tham số của Controller.
    ){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()// getFieldErrors: Trả về một danh sách các lỗi trong BindingResult. Mỗi lỗi là một đối tượng FieldErrors.
                    .stream() // stream(): Phương thức này chuyển đổi danh sách các lỗi thành một luồng (stream) các lỗi.
                    .map(FieldError::getDefaultMessage) // Phương thức map() ánh xạ mỗi FieldError trong luồng thành một chuỗi, bằng cách sử dụng phương thức getDefaultMessage() của đối tượng FieldError.
                    .toList(); // Thu thập tất cả các phần tử trong luồng và tạo thành một danh sách mới các lỗi dưới dạng List<String>.
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Create category successfully");
    }


    @GetMapping("")
    public ResponseEntity<CategoryResponse> getAllCategories(
    ){
        List<Category> categories = categoryService.getAllCategories();
        CategoryResponse response = new CategoryResponse(categories);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryByid(
            @PathVariable Long id
    ){
            Category categories = categoryService.getCategoryById(id);
            return ResponseEntity.ok().body(categories);
    }


    @PutMapping("/{id}") // Dấu ngoặc nhọn {id} đại diện cho một giá trị động (ID của danh mục) được truyền vào trong URL
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ){
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("update category successfully!");
    }
    //Note: @PathVariable: được sử dụng để trích xuất giá trị này từ URL và chuyển nó thành tham số phương thức
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category successfully!");
    }
}
