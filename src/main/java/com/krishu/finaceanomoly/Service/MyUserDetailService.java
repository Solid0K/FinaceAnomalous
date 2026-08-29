package com.krishu.finaceanomoly.Service;

import com.krishu.finaceanomoly.CustomException.NotFoundException;
import com.krishu.finaceanomoly.Model.Client;
import com.krishu.finaceanomoly.Repository.ClientRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final ClientRepo clientRepo;

    public MyUserDetailService(ClientRepo clientRepo){
        this.clientRepo=clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client=clientRepo.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        return User.builder().username(client.getEmail()).
                password(client.getPassword()).
                authorities(new SimpleGrantedAuthority("ROLE_" + client.getRole().name())).build();
    }
}
