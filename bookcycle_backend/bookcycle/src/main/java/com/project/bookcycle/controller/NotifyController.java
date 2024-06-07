package com.project.bookcycle.controller;

import com.project.bookcycle.dto.NotifyDTO;
import com.project.bookcycle.model.NotifyEntity;
import com.project.bookcycle.response.NotifyListResponse;
import com.project.bookcycle.response.NotifyResponse;
import com.project.bookcycle.service.INotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/notifies")
@RequiredArgsConstructor
public class NotifyController {
    private final INotifyService notifyService;

    @PostMapping("")
    public ResponseEntity<?> createNotify(
            @RequestBody NotifyDTO notifyDTO
    ){
        try {
            NotifyEntity notifyEntity = notifyService.createNotify(notifyDTO);
            return ResponseEntity.ok().body(NotifyResponse.builder()
                    .id(notifyEntity.getId())
                    .content(notifyEntity.getContent())
                    .userId(notifyEntity.getUser().getId())
                    .orderId(notifyEntity.getOrder().getId())
                    .status(notifyEntity.getStatus())
                    .ec("0")
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(NotifyResponse.builder()
                        .ec("-1"));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getNotifyByUser(
            @PathVariable("id") Long userId
    ){
        try {
            List<NotifyResponse> notifyResponses = notifyService.findByUserId(userId);
            return ResponseEntity.ok().body(NotifyListResponse.builder()
                    .notifyResponseList(notifyResponses)
                    .ec("0")
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(NotifyListResponse.builder()
                    .ec("-1")
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotifyById(
            @PathVariable("id") Long id
    ){
        try {
            notifyService.deleteNotifyEntity(id);
            return ResponseEntity.ok().body(NotifyResponse.builder()
                    .ec("0")
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(NotifyResponse.builder()
                    .ec("-1")
                    .build());
        }
    }
}
