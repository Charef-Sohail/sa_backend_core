package org.piteam.sa_backend_core.Mappers;

import org.piteam.sa_backend_core.dto.profile.ProfileCreateRequest;
import org.piteam.sa_backend_core.dto.profile.ProfileResponse;
import org.piteam.sa_backend_core.dto.profile.ProfileUpdateRequest;
import org.piteam.sa_backend_core.models.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMapper {
    public Profile toEntity(ProfileCreateRequest req, String studentId) {
        return Profile.builder()
                .studentId(studentId)
                .sleepHours(req.getSleepHours())
                .availability(req.getAvailability() != null ? req.getAvailability() : List.of())
                .baselineEnergy(req.getBaselineEnergy())
                .build();
    }

    public void updateEntity(ProfileUpdateRequest req, Profile profile) {
        if (req.getSleepHours() != null) profile.setSleepHours(req.getSleepHours());
        if (req.getAvailability() != null) profile.setAvailability(req.getAvailability());
        if (req.getBaselineEnergy() != null) profile.setBaselineEnergy(req.getBaselineEnergy());
    }

    public ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .studentId(profile.getStudentId())
                .sleepHours(profile.getSleepHours())
                .availability(profile.getAvailability())
                .baselineEnergy(profile.getBaselineEnergy())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .healthySleepPattern(profile.isHealthySleepPattern())
                .build();
    }

    public List<ProfileResponse> toResponseList(List<Profile> profiles) {
        return profiles.stream().map(this::toResponse).toList();
    }
}
