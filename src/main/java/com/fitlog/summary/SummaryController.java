package com.fitlog.summary;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {
    private final SummaryService summaryService;
    public SummaryController(SummaryService summaryService) { this.summaryService = summaryService; }
    @GetMapping("/daily/{userId}")
    public SummaryDTO daily(@PathVariable Long userId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return summaryService.daily(userId, date);
    }
    @GetMapping("/weekly/{userId}")
    public SummaryDTO weekly(@PathVariable Long userId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return summaryService.weekly(userId, date);
    }
}
