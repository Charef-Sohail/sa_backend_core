package org.piteam.sa_backend_core.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.faq.FaqQuestionRequest;
import org.piteam.sa_backend_core.dto.faq.FaqRequest;
import org.piteam.sa_backend_core.dto.faq.FaqResponse;
import org.piteam.sa_backend_core.services.FaqService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // ── User endpoints ─────────────────────────────────

    @GetMapping("/api/faq")
    public ResponseEntity<List<FaqResponse>> getAll() {
        return ResponseEntity.ok(faqService.getAll());
    }

    @GetMapping("/api/faq/{id}")
    public ResponseEntity<FaqResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(faqService.getById(id));
    }

    @GetMapping("/api/faq/category/{category}")
    public ResponseEntity<List<FaqResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(faqService.getByCategory(category));
    }

    @GetMapping("/api/faq/search")
    public ResponseEntity<List<FaqResponse>> search(@RequestParam String q) {
        return ResponseEntity.ok(faqService.search(q));
    }

    @PostMapping("/api/faq/questions")
    public ResponseEntity<FaqResponse> submitQuestion(@RequestBody @Valid FaqQuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(faqService.submitQuestion(request.getQuestion()));
    }

    // ── Admin endpoints ──────────────────────────────────

    @GetMapping("/api/admin/faq")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<FaqResponse>> getAllForAdmin() {
        return ResponseEntity.ok(faqService.getAllForAdmin());
    }

    @PostMapping("/api/admin/faq")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<FaqResponse> create(@Valid @RequestBody FaqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.create(request));
    }

    @PutMapping("/api/admin/faq/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<FaqResponse> update(
            @PathVariable String id,
            @Valid @RequestBody FaqRequest request) {
        return ResponseEntity.ok(faqService.update(id, request));
    }

    @DeleteMapping("/api/admin/faq/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        faqService.delete(id);
        return ResponseEntity.noContent().build();
    }
}