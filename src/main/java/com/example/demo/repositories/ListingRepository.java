package com.example.demo.repositories;

import com.example.demo.models.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.ListingStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {
    Page<Listing> findAllByDeletedFalseAndStatus(Pageable pageable, ListingStatus status);
    Optional<Listing> findByIdAndDeletedFalseAndStatus(UUID id, ListingStatus status);
}
