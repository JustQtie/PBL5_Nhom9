package com.project.bookcycle.controller;

import com.project.bookcycle.service.INotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/notifies")
@RequiredArgsConstructor
public class NotifyController {
    private final INotifyService notifyService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getNotifyByUser(
            @PathVariable("id") Long userId
    ){
        try {
            return ResponseEntity.ok().body(notifyService.findByUserId(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotifyById(
            @PathVariable("id") Long id
    ){
        try {
            notifyService.deleteNotifyEntity(id);
            return ResponseEntity.ok().body("Delete success");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
