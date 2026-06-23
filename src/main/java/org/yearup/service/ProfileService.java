package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Optional<Profile> getProfileById(int userId){ return profileRepository.findById(userId); }

    public Profile updateProfile(int userId, Profile updateProfile){

        Profile existingProfile = getProfileById(userId).orElseThrow();

        existingProfile.setFirstName(updateProfile.getFirstName());
        existingProfile.setLastName(updateProfile.getLastName());
        existingProfile.setPhone(updateProfile.getPhone());
        existingProfile.setEmail(updateProfile.getEmail());
        existingProfile.setAddress(updateProfile.getAddress());
        existingProfile.setCity(updateProfile.getCity());
        existingProfile.setState(updateProfile.getState());
        existingProfile.setZip(updateProfile.getZip());

        return profileRepository.save(existingProfile);
    }
}
