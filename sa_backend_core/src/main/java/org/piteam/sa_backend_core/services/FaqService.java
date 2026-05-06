package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.dto.faq.FaqRequest;
import org.piteam.sa_backend_core.dto.faq.FaqResponse;
import org.piteam.sa_backend_core.exceptions.ResourceNotFoundException;
import org.piteam.sa_backend_core.models.Faq;
import org.piteam.sa_backend_core.repositories.FaqRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    // ── User ──────────────────────────────────────────

    public List<FaqResponse> getAll() {
        return faqRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FaqResponse getById(String id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));
        return toResponse(faq);
    }

    public List<FaqResponse> getByCategory(String category) {
        return faqRepository.findByCategory(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<FaqResponse> search(String keyword) {
        return faqRepository.searchByKeyword(keyword)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Admin ────────────────────────────────────────────

    public FaqResponse create(FaqRequest request) {
        Faq faq = new Faq();
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        faq.setCategory(request.getCategory());
        faq.setCreatedAt(Instant.now());
        faq.setUpdatedAt(Instant.now());

        return toResponse(faqRepository.save(faq));
    }

    public FaqResponse update(String id, FaqRequest request) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));

        if (request.getQuestion() != null) {
            faq.setQuestion(request.getQuestion());
        }
        if (request.getAnswer() != null) {
            faq.setAnswer(request.getAnswer());
        }
        if (request.getCategory() != null) {
            faq.setCategory(request.getCategory());
        }
        faq.setUpdatedAt(Instant.now());

        return toResponse(faqRepository.save(faq));
    }

    public void delete(String id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with id: " + id));
        faqRepository.delete(faq);
    }

    // ── Mapper ───────────────────────────────────────────

    private FaqResponse toResponse(Faq faq) {
        return new FaqResponse(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCategory(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}