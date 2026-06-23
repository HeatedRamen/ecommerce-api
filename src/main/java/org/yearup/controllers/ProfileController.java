package org.yearup.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.service.ProfileService;

import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){ this.profileService = profileService; }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public List<Profile> getAll(){ return profileService.getAllProfile(); }

//    @PutMapping("")
//    @PreAuthorize("isAuthenticated()")
//    public Profile updateProfile(@RequestBody Profile profile){
//
//    }
}
