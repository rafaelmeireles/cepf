package br.com.rmc.security;

import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.rmc.security.user.User;
import br.com.rmc.security.user.UserRepository;

@Component
public class BaseUserDetailsService implements UserDetailsService {

    private UserRepository repository;

    public BaseUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user = new User(username);
    	Example<User> example = Example.of(user);
        return this.repository.findOne(example).orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }	
}
