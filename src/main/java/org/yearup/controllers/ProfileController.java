package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService){
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Profile> getUserProfile(Principal principal){

        int userId = getUserId(principal);

        return profileService.getProfileById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public Profile updateProfile(@RequestBody Profile profile, Principal principal){

        int userId = getUserId(principal);

        return profileService.updateProfile(userId, profile);
    }

    // Helper to get user ID
    private int getUserId(Principal principal){

        String userName = principal.getName();

        User user = userService.getByUserName(userName);
        return user.getId();
    }
}
