package com.webapp.service;

import com.webapp.domain.Profile;
import com.webapp.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<Profile> findById(Long id){
        return profileRepository.findById(id);
    }
    public void save(Profile profile) { profileRepository.save(profile);}
}
