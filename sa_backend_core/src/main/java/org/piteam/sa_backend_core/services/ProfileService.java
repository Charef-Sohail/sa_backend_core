package org.piteam.sa_backend_core.services;

import lombok.RequiredArgsConstructor;
import org.piteam.sa_backend_core.Mappers.ProfileMapper;
import org.piteam.sa_backend_core.dto.profile.ProfileCreateRequest;
import org.piteam.sa_backend_core.dto.profile.ProfileResponse;
import org.piteam.sa_backend_core.dto.profile.ProfileUpdateRequest;
import org.piteam.sa_backend_core.exceptions.ResourceNotFoundException;
import org.piteam.sa_backend_core.models.Profile;
import org.piteam.sa_backend_core.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Transactional
    public ProfileResponse createProfile(ProfileCreateRequest request, String studentId) {

        if (profileRepository.existsByStudentId(studentId)) {
            throw new IllegalStateException("Un profil existe déjà pour cet étudiant.");
        }

        Profile profile = profileMapper.toEntity(request, studentId);
        Profile saved = profileRepository.save(profile);
        return profileMapper.toResponse(saved);
    }

    public ProfileResponse getProfileByStudentId(String studentId) {
        Profile profile = profileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvée pour cet étudiant : "+ studentId));
        return profileMapper.toResponse(profile);
    }

    @Transactional
    public ProfileResponse updateProfile(String studentId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvée pour cet étudiant : "+ studentId));

        profileMapper.updateEntity(request, profile);
        Profile updated = profileRepository.save(profile);
        return profileMapper.toResponse(updated);
    }

    @Transactional
    public void deleteProfile(String studentId) {
        if (!profileRepository.existsByStudentId(studentId)) {
            throw new ResourceNotFoundException("Profil non trouvée pour cet étudiant: "+ studentId);
        }
        profileRepository.deleteByStudentId(studentId);
    }
}
