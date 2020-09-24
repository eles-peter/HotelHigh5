package com.progmasters.hotel.security;

import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JPAUserDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public JPAUserDetailsService(AccountRepository repository) {
        this.accountRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(name);

        if (account == null) {
            throw new UsernameNotFoundException("No person found with this name: " + name);
        }
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(account.getRole().toString());
        UserDetails principal = User.withUsername(name).authorities(authorities).password(account.getPassword()).build();

        return principal;
    }
}
