package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.DeleteSelectedOfUserIdRequest;
import com.finale.ConferenceManagement.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @GetMapping("/adminId={id}/count/conference/status={status}")
    public ResponseEntity<?> getCountConferencesByStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        return ResponseEntity.ok(adminService.countConferencesByStatus(status));
    }

    @GetMapping("/adminId={id}/count/user/type={type}")
    public ResponseEntity<?> getCountUsersByType(@PathVariable("id") String id, @PathVariable("type") String type) {
        return ResponseEntity.ok(adminService.countUsersByType(type));
    }

    @GetMapping("/adminId={id}/conference")
    public ResponseEntity<?> getConferencesByAdminId(@PathVariable("id") String id) {
        return ResponseEntity.ok(adminService.findConferencesByAdminId(id));
    }

    @GetMapping("/adminId={id}/paper")
    public ResponseEntity<?> getPapersByAdminId(@PathVariable("id") String id) {
        return ResponseEntity.ok(adminService.findPapersByAdminId(id));
    }

    @GetMapping("/adminId={id}/user")
    public ResponseEntity<?> getUsersByAdminId(@PathVariable("id") String id) {
        return ResponseEntity.ok(adminService.findUsersByAdminId(id));
    }

    @DeleteMapping("/adminId={id}/conference")
    public ResponseEntity<?> deleteSelectedConferencesByAdminId(@PathVariable("id") String id, 
            @Validated @RequestBody DeleteSelectedOfUserIdRequest request) {
        try {
            adminService.deleteSelectedConferencesByAdminId(id, request.getIds());
            return ResponseEntity.ok("Delete Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/adminId={id}/user")
    public ResponseEntity<?> deleteSelectedUsersByAdminId(@PathVariable("id") String id, 
            @Validated @RequestBody DeleteSelectedOfUserIdRequest request) {
        try {
            adminService.deleteSelectedUsersByAdminId(id, request.getIds());
            return ResponseEntity.ok("Delete Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
