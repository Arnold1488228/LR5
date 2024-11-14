package ua.com.lr5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.lr5.entity.User;
import ua.com.lr5.repository.RoleRepository;
import ua.com.lr5.repository.RolesHasUsersRepository;
import ua.com.lr5.repository.UserRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RolesHasUsersRepository rolesHasUsersRepository;
    private final RoleRepository roleRepository;


    public Flux<GrantedAuthority> getAuthorities(Long userId) {
        return rolesHasUsersRepository.findRoleIdsByUserId(userId)
                .flatMap(roleId -> roleRepository.findById(roleId))
                .map(role -> role).cast(GrantedAuthority.class);
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user ->
                        getAuthorities(user.getId())
                                .collectList()
                                .map(authorities -> {
                                    user.setAuthorities(new HashSet<>(authorities));
                                    return user;
                                })
                );
    }


    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
