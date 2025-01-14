package org.example.repository;

import org.example.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount,Long> {

    @Query("select a from BankAccount a where a.user.id = :id")
    List<BankAccount> findByClientId(UUID id);

    @Query("select a from BankAccount a where a.number = :accountNumber")
    Optional<BankAccount> findByAccountNumber(Long accountNumber);
}
