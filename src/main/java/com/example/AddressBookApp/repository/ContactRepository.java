package com.example.AddressBookApp.repository;

import com.example.AddressBookApp.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    public ContactEntity findByEmail(String email);
}