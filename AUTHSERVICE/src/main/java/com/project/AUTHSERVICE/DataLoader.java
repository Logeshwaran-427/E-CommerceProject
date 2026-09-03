package com.project.AUTHSERVICE;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.AUTHSERVICE.Entity.SellerProfile;
import com.project.AUTHSERVICE.Entity.UserDet;
import com.project.AUTHSERVICE.Enums.RoleEnum;
import com.project.AUTHSERVICE.Enums.SellerStatus;
import com.project.AUTHSERVICE.Repository.SellerRepo;
import com.project.AUTHSERVICE.Repository.UserDetRepo;

@Component
public class DataLoader implements CommandLineRunner {

    final UserDetRepo userDetRepo;

    final PasswordEncoder passwordEncoder;

    final SellerRepo sellerRepo;

    DataLoader(UserDetRepo userDetRepo, PasswordEncoder passwordEncoder, SellerRepo sellerRepo) {
        this.userDetRepo = userDetRepo;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        UserDet userDet=new UserDet();
				userDet.setUsername("Loki");
				userDet.setPassword(passwordEncoder.encode("Logesh@427"));
				userDet.setEmail("Logesh@gmail.com");
				userDet.setPhoneNumber("9876543211");
				userDet.setRole(RoleEnum.ADMIN);
				userDetRepo.save(userDet);



        // ADMIN
        UserDet admin = new UserDet();
        admin.setUsername("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setPhoneNumber("9999999991");
        admin.setRole(RoleEnum.ADMIN);

        userDetRepo.save(admin);

        // NORMAL USER
        UserDet user = new UserDet();
        user.setUsername("user1");
        user.setEmail("user1@gmail.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setPhoneNumber("9999999992");
        user.setRole(RoleEnum.USER);

        userDetRepo.save(user);

        // APPROVED SELLER 1
        UserDet seller1 = new UserDet();
        seller1.setUsername("seller1");
        seller1.setEmail("seller1@gmail.com");
        seller1.setPassword(passwordEncoder.encode("seller123"));
        seller1.setPhoneNumber("9999999993");
        seller1.setRole(RoleEnum.PRODUCT_OWNER);

        userDetRepo.save(seller1);

        SellerProfile sellerProfile1 = new SellerProfile();
        sellerProfile1.setBusinessName("Logesh Electronics");
        sellerProfile1.setBusinessEmail("business1@gmail.com");
        sellerProfile1.setGst("GST123456");
        sellerProfile1.setStatus(SellerStatus.APPROVED);
        sellerProfile1.setUserDet(seller1);

        sellerRepo.save(sellerProfile1);

        // APPROVED SELLER 2
        UserDet seller2 = new UserDet();
        seller2.setUsername("seller2");
        seller2.setEmail("seller2@gmail.com");
        seller2.setPassword(passwordEncoder.encode("seller123"));
        seller2.setPhoneNumber("9999999994");
        seller2.setRole(RoleEnum.PRODUCT_OWNER);

        userDetRepo.save(seller2);

        SellerProfile sellerProfile2 = new SellerProfile();
        sellerProfile2.setBusinessName("Mobile World");
        sellerProfile2.setBusinessEmail("business2@gmail.com");
        sellerProfile2.setGst("GST654321");
        sellerProfile2.setStatus(SellerStatus.APPROVED);
        sellerProfile2.setUserDet(seller2);

        sellerRepo.save(sellerProfile2);

        // REJECTED SELLER
        UserDet seller3 = new UserDet();
        seller3.setUsername("User2");
        seller3.setEmail("User2@gmail.com");
        seller3.setPassword(passwordEncoder.encode("User123"));
        seller3.setPhoneNumber("9999999995");
        seller3.setRole(RoleEnum.USER);

        userDetRepo.save(seller3);

        SellerProfile sellerProfile3 = new SellerProfile();
        sellerProfile3.setBusinessName("Fake Electronics");
        sellerProfile3.setBusinessEmail("fake@gmail.com");
        sellerProfile3.setGst("GST999999");
        sellerProfile3.setStatus(SellerStatus.REJECTED);
        sellerProfile3.setUserDet(seller3);

        sellerRepo.save(sellerProfile3);

        System.out.println("Initial data loaded successfully");
    }
}
