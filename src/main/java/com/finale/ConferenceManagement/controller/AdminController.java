package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
