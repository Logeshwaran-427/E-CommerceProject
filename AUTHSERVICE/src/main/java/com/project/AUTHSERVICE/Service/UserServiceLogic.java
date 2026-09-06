package com.project.AUTHSERVICE.Service;

import java.util.Date;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.AUTHSERVICE.DTO.ChangePasswordDTO;
import com.project.AUTHSERVICE.DTO.CreateUserDTO;
import com.project.AUTHSERVICE.DTO.RefreshTokenRequest;
import com.project.AUTHSERVICE.DTO.SellerProfileDTO;
import com.project.AUTHSERVICE.DTO.TokenResponse;
import com.project.AUTHSERVICE.Entity.RefreshToken;
import com.project.AUTHSERVICE.Entity.SellerProfile;
import com.project.AUTHSERVICE.Entity.UserDet;
import com.project.AUTHSERVICE.Enums.RoleEnum;
import com.project.AUTHSERVICE.Enums.SellerStatus;
import com.project.AUTHSERVICE.Exceptions.BadRequestException;
import com.project.AUTHSERVICE.Exceptions.ResourceNotFoundException;
import com.project.AUTHSERVICE.Repository.RefreshtokenRepo;
import com.project.AUTHSERVICE.Repository.SellerRepo;
import com.project.AUTHSERVICE.Repository.UserDetRepo;
import com.project.AUTHSERVICE.UtilClasses.JwtUtility;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;

@Service
public class UserServiceLogic {

    final UserDetRepo userDetRepo;

    final PasswordEncoder passwordEncoder;

    final SellerRepo sellerRepo;

    final RefreshtokenRepo refreshtokenRepo;
    
    final JwtUtility jwtUtility;

    public static final Logger log=LoggerFactory.getLogger(UserServiceLogic.class);

    UserServiceLogic(UserDetRepo userDetRepo,JwtUtility jwtUtility,  RefreshtokenRepo refreshtokenRepo, PasswordEncoder passwordEncoder, SellerRepo sellerRepo) {
        this.userDetRepo = userDetRepo;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepo = sellerRepo;
        this.refreshtokenRepo=refreshtokenRepo;
        this.jwtUtility=jwtUtility;
    }

    //Get user by name

    public UserDet getUserByName(String name){
        log.info("Loading user details for username={}", name);
        return userDetRepo.findByUsername(name).get();
    }

    //Creating new user 

    public ResponseEntity<String> createNewUser(CreateUserDTO user){
        log.info("New user registration request received for username={}", user.getUsername());

            if(userDetRepo.existsByUsername(user.getUsername())){
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("Username already exists");
            }

            if(userDetRepo.existsByEmail(user.getEmail())){
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("Email already registered");
            }

            UserDet userDet=new UserDet();
            userDet.setUsername(user.getUsername());
            userDet.setPassword(passwordEncoder.encode(user.getPassword()));
            userDet.setEmail(user.getEmail());
            userDet.setPhoneNumber(user.getPhoneNumber());
            userDet.setRole(RoleEnum.USER);
            userDetRepo.save(userDet);
            log.info("User account created successfully. userId={}, username={}",userDet.getId(),userDet.getUsername());
            return ResponseEntity.status(HttpStatus.SC_CREATED).body("User Saved Successfully");
        
    }
    
    //User Applying for seller

    public ResponseEntity<String> applyForSupplier(SellerProfileDTO seller){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String name=auth.getName();
        log.info("Seller application received from user={}", name);

        UserDet userDet=userDetRepo.findByUsername(name).orElseThrow();
        SellerProfile existing =sellerRepo.findByUserDet(userDet);

        if(existing != null){
            log.warn("Seller application already exists for user={}", name);
            return ResponseEntity.badRequest().body("Seller request already exists");
        }
            SellerProfile sellerProfile=new SellerProfile();
            sellerProfile.setBusinessName(seller.getBusinessName());
            sellerProfile.setBusinessEmail(seller.getBusinessEmail());
            sellerProfile.setGst(seller.getGst());
            sellerProfile.setStatus(SellerStatus.PENDING);
            sellerProfile.setUserDet(userDet);
            sellerRepo.save(sellerProfile);
            log.info("Seller application submitted successfully. sellerId={}, username={}",sellerProfile.getId(),name);
            return ResponseEntity.status(HttpStatus.SC_CREATED).body("You have applied for Supplier. It is pending with ADMIN to approve it");
        
    }

    //Get List of sellers

    public List<SellerProfile> sellerProfiles(){
        log.info("Fetching all seller applications");
        return sellerRepo.findAll();
    }

    // Pending seller requests can be approved by Admin

    public String approvePendingSeller(Long id){
        log.info("Admin processing seller approval. sellerId={}", id);
        SellerProfile seller=sellerRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Seller not found with id"+id));

        if(seller.getStatus() != SellerStatus.PENDING){
            log.warn("Seller approval skipped. sellerId={} already processed", id);
            return"Request already processed";
        }

        if(seller!=null){
            seller.setStatus(SellerStatus.APPROVED);
            UserDet user=seller.getUserDet();
            user.setRole(RoleEnum.PRODUCT_OWNER);
            sellerRepo.save(seller);
            userDetRepo.save(user);
            log.info(seller.getUserDet().getUsername()+"'s request for seller is approved");
            log.info("Seller approved successfully. sellerId={}, username={}",seller.getId(),seller.getUserDet().getUsername());
            return "Your request has been approved!";
        }

        return "Seller not found";

    }

    // Pending seller requests can be rejected by Admin

    public String rejectPendingSeller(Long id){
        log.info("Admin processing seller rejection. sellerId={}", id);
        SellerProfile seller=sellerRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Seller not found with id"+id));
        ;

        if(seller.getStatus() != SellerStatus.PENDING){
            log.warn("Seller rejection skipped. sellerId={} already processed", id);
            return"Request already processed";
        }

        if(seller!=null){
            seller.setStatus(SellerStatus.REJECTED);
            sellerRepo.save(seller);
            log.info(seller.getUserDet().getUsername()+"'s request for seller is rejected");
            log.info("Seller rejected successfully. sellerId={}, username={}",seller.getId(),seller.getUserDet().getUsername());
            return "You did not met our conditions. So, You are rejected!";
        }

        return "Seller not found";

    }

    //Get all users 

    public List<UserDet> userDets(){
        log.info("Fetching all users");
        return userDetRepo.findAll();
    }

    //Delete user

    @Transactional
    public String deleteUser(long id){
        log.info("Delete user request received. userId={}", id);
        UserDet userDet = userDetRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id"+id));
        ;

        if(userDet == null){
            return "User not exists";
        }

        if(!userDet.getIsActive()){
            log.warn("Delete request ignored. userId={} already inactive", id);
            return "User already deleted";
        }

        if(userDet.getRole() == RoleEnum.PRODUCT_OWNER){

            SellerProfile seller =sellerRepo.findByUserDet(userDet);

            if(seller != null){
                seller.setStatus(SellerStatus.INACTIVE);
                sellerRepo.save(seller);
                log.info("Seller profile deactivated. sellerId={}", seller.getId());

            }
        }

        userDet.setIsActive(false);
        userDetRepo.save(userDet);
        log.info("User deleted successfully. userId={}, username={}",userDet.getId(),userDet.getUsername());

        return "User is deleted!";
    }


    //Change password

    public String changePassword(ChangePasswordDTO changePasswordDTO){
        

        Authentication auth =SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDet userDet =userDetRepo.findByUsername(username).orElseThrow();
        log.info("Password change requested by user={}", username);
        if(!passwordEncoder.matches(
            changePasswordDTO.getOldPassword(),
            userDet.getPassword())) {
            log.warn("Password change failed. Incorrect old password for user={}", username);
        return "Old password is incorrect";
        }

        userDet.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        userDetRepo.save(userDet);
        log.info("Password changed successfully for user={}", username);
        return "Password successfully changed";
    }

    // generateRefreshToken

    public void saveRefreshToken(String refreshToken){

        Claims data=jwtUtility.extractBody(refreshToken);
        UserDet userDet=getUserByName(data.getSubject());

        RefreshToken existing =refreshtokenRepo.findByUserDet(userDet).orElse(null);

        if(existing != null){
            refreshtokenRepo.delete(existing);
        }

        RefreshToken newRefreshToken=new RefreshToken();
        newRefreshToken.setRefreshToken(refreshToken);
        newRefreshToken.setRevoked(false);
        newRefreshToken.setExpiryDate(data.getExpiration());
        newRefreshToken.setUserDet(userDet);
        refreshtokenRepo.save(newRefreshToken);
    }

    // call refresh token

    public TokenResponse refresh(RefreshTokenRequest refreshTokenRequest){

        RefreshToken token=refreshtokenRepo.findByRefreshToken(refreshTokenRequest.getRefreshToken()).orElseThrow(()-> new ResourceNotFoundException("refreshToken not found"));
        if(token.isRevoked()){
            throw new ResourceNotFoundException("Your token is already revoked");
        }

        if(token.getExpiryDate().before(new Date())){
            throw new ResourceNotFoundException("Your token is expired");
        }

        
        UserDet userDet=token.getUserDet();

        String accessToken=jwtUtility.generateToken(userDet.getUsername(),userDet.getRole().name());
        String newRefreshToken=jwtUtility.generateRefreshToken(userDet.getUsername(),userDet.getRole().name());

        TokenResponse newTokens=new TokenResponse(accessToken, newRefreshToken);

        refreshtokenRepo.delete(token);
        saveRefreshToken(newRefreshToken);



        return newTokens;
    }


    public String logOut(RefreshTokenRequest refreshTokenRequest){

        RefreshToken token=refreshtokenRepo.findByRefreshToken(refreshTokenRequest.getRefreshToken()).orElseThrow(()->new ResourceNotFoundException("Refresh token not found"));

        if (token.isRevoked()) {
            throw new BadRequestException("User already logged out");
        }
        
        token.setRevoked(true);

        refreshtokenRepo.save(token);

        return "Logged out successfully";

    }




}
