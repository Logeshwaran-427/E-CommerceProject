package com.project.AUTHSERVICE.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.AUTHSERVICE.DTO.ChangePasswordDTO;
import com.project.AUTHSERVICE.DTO.CreateUserDTO;
import com.project.AUTHSERVICE.DTO.LoginDTO;
import com.project.AUTHSERVICE.DTO.RefreshTokenRequest;
import com.project.AUTHSERVICE.DTO.SellerDetailsResponse;
import com.project.AUTHSERVICE.DTO.SellerProfileDTO;
import com.project.AUTHSERVICE.DTO.TokenResponse;
import com.project.AUTHSERVICE.DTO.UserDetailsResponseDTO;
import com.project.AUTHSERVICE.Entity.UserDet;
import com.project.AUTHSERVICE.Service.UserServiceLogic;
import com.project.AUTHSERVICE.UtilClasses.JwtUtility;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/auth")

public class UserDetController {

    
    final AuthenticationManager authenticationManager;
    final UserServiceLogic userServiceLogic;
    final JwtUtility jwtUtility;

    public UserDetController(AuthenticationManager authenticationManager, UserServiceLogic userServiceLogic,
            JwtUtility jwtUtility) {
        this.authenticationManager = authenticationManager;
        this.userServiceLogic = userServiceLogic;
        this.jwtUtility = jwtUtility;
    }

    


    public static final Logger log=LoggerFactory.getLogger(UserDetController.class);

    @PostMapping("/login")
    public TokenResponse postMethodName(@RequestBody LoginDTO loginDTO ) {
        UserDet userDet=userServiceLogic.getUserByName(loginDTO.getUsername());
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
       log.info(loginDTO.getUsername()+" logged in successfully");
       String role=userDet.getRole().name();
       String token=jwtUtility.generateToken(loginDTO.getUsername(), role);
       String refreshToken=jwtUtility.generateRefreshToken(loginDTO.getUsername(), role);
       userServiceLogic.saveRefreshToken(refreshToken);
       return new TokenResponse(token, refreshToken);
    }




// {
//     "username":"Abhi",
//     "password":"Abhi@427",
//     "email":"Abhi@tcs.com",
//     "phoneNumber":"9876543211"
// }
    
    @PostMapping("/createUser")
    public ResponseEntity<String> postMethodName(@RequestBody @Valid  CreateUserDTO user) {
        return userServiceLogic.createNewUser(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/applySeller")
    public ResponseEntity<String> applySeller(@RequestBody @Valid SellerProfileDTO sellerProfile) {  
        return userServiceLogic.applyForSupplier(sellerProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getSeller")
    public List<SellerDetailsResponse> getSeller() {
        return userServiceLogic.sellerProfiles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approveSeller/{id}")
    public String approveSeller(@PathVariable Long id) { 
        return userServiceLogic.approvePendingSeller(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rejectSeller/{id}")
    public String rejectSeller(@PathVariable Long id) { 
        return userServiceLogic.rejectPendingSeller(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getUsers")
    public List<UserDetailsResponseDTO> getUsers() {
        return userServiceLogic.userDets();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id){
        return userServiceLogic.deleteUser(id);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @PatchMapping("/changePassword")
    public String changePassword(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userServiceLogic.changePassword(changePasswordDTO);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER','ADMIN')")
    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return userServiceLogic.refresh(refreshTokenRequest);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER','ADMIN')")
    @PostMapping("/logout")
    public String logout(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return userServiceLogic.logOut(refreshTokenRequest);
    }
    
    
}
