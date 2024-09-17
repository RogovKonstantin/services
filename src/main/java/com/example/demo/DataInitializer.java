package com.example.demo;

import com.example.demo.models.Category;
import com.example.demo.models.Listing;
import com.example.demo.models.User;
import com.example.demo.models.AuditLog;
import com.example.demo.models.ListingStatus;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ListingRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private ListingRepository listingRepository;
    private AuditLogRepository auditLogRepository;

    @Override
    public void run(String... args) {
        // Create Users
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

        // Create Categories
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

        // Create Listings and corresponding AuditLogs
        createListingWithAuditLog("Samsung Galaxy S21", "Latest Samsung smartphone with excellent features.", new BigDecimal("799.99"), "New York", user1, category1);
        createListingWithAuditLog("Wooden Dining Table", "Stylish wooden dining table for modern homes.", new BigDecimal("499.99"), "Los Angeles", user2, category2);
        createListingWithAuditLog("Mystery Novel Collection", "A collection of mystery novels perfect for book lovers.", new BigDecimal("29.99"), "Chicago", user3, category3);
        createListingWithAuditLog("Kids Toy Set", "A fun toy set for kids ages 3-5.", new BigDecimal("19.99"), "Miami", user4, category4);
        createListingWithAuditLog("Leather Jacket", "Stylish leather jacket for adults.", new BigDecimal("199.99"), "San Francisco", user5, category5);
        createListingWithAuditLog("Vintage Record Player", "High-quality vintage record player for music enthusiasts.", new BigDecimal("249.99"), "Austin", user6, category1);
        createListingWithAuditLog("Office Desk", "Spacious office desk with a modern look.", new BigDecimal("349.99"), "New York", user1, category2);
        createListingWithAuditLog("Learning Python Book", "A comprehensive guide to learning Python programming.", new BigDecimal("34.99"), "Chicago", user3, category3);
        createListingWithAuditLog("Plush Teddy Bear", "Soft teddy bear perfect for children of all ages.", new BigDecimal("15.00"), "Miami", user4, category4);
        createListingWithAuditLog("Fashionable Summer Dress", "Stylish summer dress available in multiple sizes.", new BigDecimal("59.99"), "San Francisco", user5, category5);

        // Log to console
        System.out.println("Database has been populated with initial data.");
    }

    // Helper method to create a listing and audit log
    private void createListingWithAuditLog(String title, String description, BigDecimal price, String location, User user, Category category) {
        Listing listing = new Listing();
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setPrice(price); // update to accept BigDecimal
        listing.setLocation(location);
        listing.setUser(user);
        listing.setCategory(category);
        listing.setStatus(ListingStatus.PENDING);
        listingRepository.save(listing);

        // Create corresponding audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("CREATE");
        auditLog.setListing(listing);
        auditLog.setDetails("Created listing for: " + title);
        auditLogRepository.save(auditLog);
    }

    @Autowired
    public void setListingRepository(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Autowired
    public void setAuditLogRepository(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
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
