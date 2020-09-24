package com.progmasters.hotel.repository;

import com.progmasters.hotel.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByEmail(String email);

    Account findByUsername(String username);

    List<Account> findAllByEmail(String email);

}
