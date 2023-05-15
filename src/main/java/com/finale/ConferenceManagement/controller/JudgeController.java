package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.service.JudgeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/judge")
public class JudgeController {
    private JudgeService judgeService;

    @GetMapping("/judgeId={id}/paper")
    public ResponseEntity<?> getPapersByJudgeId(@PathVariable("id") String id) {
        return ResponseEntity.ok(judgeService.findPaperByJudgeId(id));
    }

    @GetMapping("/judgeId={id}/count/paper/status={status}")
    public ResponseEntity<?> getCountPaperByJudgeIdAndStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        return ResponseEntity.ok(judgeService.countPaperByJudgeIdAndStatus(id, status));
    }
}
