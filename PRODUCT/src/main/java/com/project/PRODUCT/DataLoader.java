package com.project.PRODUCT;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.netflix.discovery.converters.Auto;
import com.project.PRODUCT.Entity.Categories;
import com.project.PRODUCT.Entity.Product;
import com.project.PRODUCT.Enums.ProductStatus;
import com.project.PRODUCT.Repository.CategoryRepository;
import com.project.PRODUCT.Repository.ProductRepository;

@Component
public class DataLoader implements CommandLineRunner  {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

	@Override
	public void run(String... args) throws Exception {
		if(categoryRepository.count()==0 && productRepository.count()==0){

				Categories category1=new Categories();
				category1.setName("Electronics");
				category1.setDescription("Products that needs electricity");
				categoryRepository.save(category1);

				Categories category2=new Categories();
				category2.setName("Fashion");
				category2.setDescription("Clothing, footwear, and fashion accessories");
				categoryRepository.save(category2);

				Categories category3=new Categories();
				category3.setName("Home & Kitchen");
				category3.setDescription("Household essentials, furniture, and kitchen appliances");
				categoryRepository.save(category3);

				Categories category4=new Categories();
				category4.setName("Books");
				category4.setDescription("Educational, fiction, and non-fiction books");
				categoryRepository.save(category4);

				Categories category5=new Categories();
				category5.setName("Sports & Fitness");
				category5.setDescription("Sports equipment, fitness gear, and outdoor products");
				categoryRepository.save(category5);

				Categories category6=new Categories();
				category6.setName("Beauty & Personal Care");
				category6.setDescription("Cosmetics, skincare, and personal grooming products");
				categoryRepository.save(category6);

				Categories category7=new Categories();
				category7.setName("Toys & Games");
				category7.setDescription("Toys, board games, and entertainment products for all ages");
				categoryRepository.save(category7);
			
                createProduct("iPhone 15", "Apple smartphone with A16 chip",
                         25000.0, "Apple", category1, 1L);

                createProduct("Galaxy S24", "Samsung flagship smartphone",
                        74999.0,  "Samsung", category1, 2L);

                createProduct("MacBook Air M3", "Lightweight Apple laptop",
                        124999.0,  "Apple", category1, 1L);

                createProduct("Sony WH-1000XM5", "Noise cancelling headphones",
                        29999.0,  "Sony", category1, 2L);

                createProduct("Dell Inspiron 15", "15-inch productivity laptop",
                        65999.0,  "Dell", category1, 1L);

                createProduct("Denim Jacket", "Classic blue denim jacket",
                        2499.0,  "Levis", category2, 2L);
                
                createProduct("Running Shoes", "Comfortable sports shoes",
                        3999.0,  "Nike", category2, 1L);
                
                createProduct("Casual T Shirt", "Cotton round neck t-shirt",
                        699.0,  "Puma", category2, 2L);
                
                createProduct("Formal Shirt", "Slim fit office shirt",
                        1499.0,  "Allen Solly", category2, 1L);
                
                createProduct("Handbag", "Premium leather handbag",
                        2999.0,  "Lavie", category2, 2L);       

                createProduct("Air Fryer", "Healthy oil-free cooking appliance",
                        6999.0,  "Philips", category3, 1L);
                
                createProduct("Microwave Oven", "25L convection microwave",
                        11999.0,  "LG", category3, 2L);
                
                createProduct("Mixer Grinder", "750W kitchen mixer",
                        3499.0,  "Prestige", category3, 1L);
                
                createProduct("Dining Chair", "Wooden dining chair",
                        2499.0,  "Home Centre", category3, 2L);
                
                createProduct("Cookware Set", "Non-stick cookware set",
                        2999.0,  "Pigeon", category3, 1L);
                
                createProduct("Clean Code", "Guide to writing clean software",
                        799.0,  "Prentice Hall", category4, 2L);
                
                createProduct("Effective Java", "Java best practices",
                        999.0,  "Addison Wesley", category4, 1L);
                
                createProduct("Atomic Habits", "Build better habits",
                        599.0,  "Penguin", category4, 2L);
                
                createProduct("The Pragmatic Programmer", "Software craftsmanship",
                        899.0, "Addison Wesley", category4, 1L);
                
                createProduct("Rich Dad Poor Dad", "Personal finance classic",
                        499.0,  "Plata", category4, 2L);

                createProduct("Yoga Mat", "Anti-slip yoga mat",
                        999.0,  "Boldfit", category5, 1L);
                
                createProduct("Dumbbell Set", "20kg adjustable dumbbells",
                        4999.0,  "Kore", category5, 2L);
                
                createProduct("Cricket Bat", "English willow cricket bat",
                        5999.0,  "SS", category5, 1L);
                
                createProduct("Football", "Professional football",
                        1499.0,  "Nivia", category5, 2L);
                
                createProduct("Treadmill", "Motorized treadmill",
                        29999.0,  "PowerMax", category5, 1L);
                
                createProduct("Face Wash", "Daily skincare face wash",
                        299.0,  "Nivea", category6, 2L);
                
                createProduct("Hair Dryer", "1200W hair dryer",
                        1499.0,  "Philips", category6, 1L);
                
                createProduct("Perfume", "Long lasting fragrance",
                        2499.0,  "Fogg", category6, 2L);
                
                createProduct("Moisturizer", "Hydrating skin cream",
                        399.0,  "Cetaphil", category6, 1L);
                
                createProduct("Electric Trimmer", "Rechargeable trimmer",
                        1999.0,  "Philips", category6, 2L);

                createProduct("Remote Control Car", "Rechargeable RC car",
                        2499.0,  "Hot Wheels", category7, 1L);
                
                createProduct("Lego Building Set", "Creative building blocks",
                        3999.0,  "LEGO", category7, 2L);
                
                createProduct("Chess Board", "Wooden chess set",
                        999.0,  "ChessMaster", category7, 1L);
                
                createProduct("Barbie Doll", "Fashion doll",
                        1499.0,  "Mattel", category7, 2L);
                
                createProduct("Puzzle Game", "1000-piece puzzle",
                        799.0, "Funskool", category7, 1L);
            
        }
	}


    private void createProduct(String name,String description,Double price,String brand,Categories category,Long sellerId) {

    Product product = new Product();
    product.setName(name);
    product.setDescription(description);
    product.setPrice(price);
//     product.setStockQuantity(stock);
    product.setSku(brand+"-"+name+"-"+UUID.randomUUID().toString().substring(0,4));
    product.setBrand(brand);
    product.setCategory(category);
    product.setSellerId(sellerId);
    product.setStatus(ProductStatus.ACTIVE);

    productRepository.save(product);
    }

    
}
