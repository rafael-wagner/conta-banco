package org.example.repository;

import org.example.entity.AccountBalanceMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceMovementRepository extends JpaRepository<AccountBalanceMovement, Long> {

}
