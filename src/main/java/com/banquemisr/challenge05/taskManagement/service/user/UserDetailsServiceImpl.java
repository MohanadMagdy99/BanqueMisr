package com.banquemisr.challenge05.taskManagement.service.user;


import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.repositories.UserRepository;
import com.banquemisr.challenge05.taskManagement.security.UserPrincipal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserEntity> user = userRepository.findFirstByUsername(username);
    if (user.isPresent()) {
      return UserPrincipal.build(user.get());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

}
