package org.zimid.adminservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zimid.adminservice.model.AdminUser;
import org.zimid.adminservice.repository.AdminUserRepository;

@Service
@RequiredArgsConstructor
public class CustomAdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found: " + username));

        return AdminUserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Admin user not found with id: " + id));

        return AdminUserPrincipal.create(user);
    }
}