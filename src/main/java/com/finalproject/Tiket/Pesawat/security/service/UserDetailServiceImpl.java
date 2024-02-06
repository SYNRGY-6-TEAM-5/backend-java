package com.finalproject.Tiket.Pesawat.security.service;

import com.finalproject.Tiket.Pesawat.model.EnumRole;
import com.finalproject.Tiket.Pesawat.model.Role;
import com.finalproject.Tiket.Pesawat.model.User;
import com.finalproject.Tiket.Pesawat.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.finalproject.Tiket.Pesawat.utils.Constants.CONSTANT_EMAIL_TEST_FORGOT;

@Service
@Log4j2
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        if (username.equals(CONSTANT_EMAIL_TEST_FORGOT)) {
            // Create and return a dummy user
            User dummyUser = User.builder()
                    .uuid(UUID.randomUUID())
                    .fullname("Dummy User")
                    .emailAddress(CONSTANT_EMAIL_TEST_FORGOT)
                    .isActive(true)
                    .role(Role.builder().roleName(EnumRole.USER).build())
                    .birthDate(new Date())
                    .phoneNumber(1234567890L)
                    .createdAt(new Date())
                    .lastModified(new Date())
                    .build();

            return UserDetailsImpl.build(dummyUser);
        }
        User user = userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found : " + username));

        return UserDetailsImpl.build(user);
    }
}
