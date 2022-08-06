package com.example.hishab.hishabApp.service;

import com.example.hishab.hishabApp.model.UserDetailsImpl;
import com.example.hishab.hishabApp.model.UserModel;
import com.example.hishab.hishabApp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService {
    IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findFirstByMobileNo(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + username));
        return new UserDetailsImpl(userModel);
    }
}
