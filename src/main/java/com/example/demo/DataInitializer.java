package com.example.demo;

import com.example.demo.models.Category;
import com.example.demo.models.Listing;
import com.example.demo.models.User;
import com.example.demo.models.ListingStatus;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public class DataInitializer implements CommandLineRunner {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ListingRepository listingRepository;

    @Override
    public void run(String... args) {
        User user1 = new User();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("jane_doe");
        user2.setEmail("jane@example.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("alice_smith");
        user3.setEmail("alice@example.com");
        userRepository.save(user3);

        User user4 = new User();
        user4.setUsername("bob_johnson");
        user4.setEmail("bob@example.com");
        userRepository.save(user4);

        User user5 = new User();
        user5.setUsername("charlie_brown");
        user5.setEmail("charlie@example.com");
        userRepository.save(user5);

        User user6 = new User();
        user6.setUsername("dave_wilson");
        user6.setEmail("dave@example.com");
        userRepository.save(user6);

        Category category1 = new Category();
        category1.setName("Electronics");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Furniture");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Books");
        categoryRepository.save(category3);

        Category category4 = new Category();
        category4.setName("Toys");
        categoryRepository.save(category4);

        Category category5 = new Category();
        category5.setName("Clothing");
        categoryRepository.save(category5);

        createListing("Samsung Galaxy S21", "Latest Samsung smartphone with excellent features.", new BigDecimal("799.99"), "New York", user1, category1);
        createListing("Wooden Dining Table", "Stylish wooden dining table for modern homes.", new BigDecimal("499.99"), "Los Angeles", user2, category2);
        createListing("Mystery Novel Collection", "A collection of mystery novels perfect for book lovers.", new BigDecimal("29.99"), "Chicago", user3, category3);
        createListing("Kids Toy Set", "A fun toy set for kids ages 3-5.", new BigDecimal("19.99"), "Miami", user4, category4);
        createListing("Leather Jacket", "Stylish leather jacket for adults.", new BigDecimal("199.99"), "San Francisco", user5, category5);
        createListing("Vintage Record Player", "High-quality vintage record player for music enthusiasts.", new BigDecimal("249.99"), "Austin", user6, category1);
        createListing("Office Desk", "Spacious office desk with a modern look.", new BigDecimal("349.99"), "New York", user1, category2);
        createListing("Learning Python Book", "A comprehensive guide to learning Python programming.", new BigDecimal("34.99"), "Chicago", user3, category3);
        createListing("Plush Teddy Bear", "Soft teddy bear perfect for children of all ages.", new BigDecimal("15.00"), "Miami", user4, category4);
        createListing("Fashionable Summer Dress", "Stylish summer dress available in multiple sizes.", new BigDecimal("59.99"), "San Francisco", user5, category5);

        System.out.println("Database has been populated with initial data.");
    }

    private void createListing(String title, String description, BigDecimal price, String location, User user, Category category) {
        Listing listing = new Listing();
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setPrice(price);
        listing.setLocation(location);
        listing.setUser(user);
        listing.setCategory(category);
        listing.setStatus(ListingStatus.ACCEPTED);
        listingRepository.save(listing);
    }



    @Autowired
    public void setListingRepository(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
