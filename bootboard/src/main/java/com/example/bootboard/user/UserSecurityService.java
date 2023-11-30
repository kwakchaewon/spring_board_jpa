package com.example.bootboard.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * loadUserByUsername: 사용자명으로 비밀번호 조회 후 리턴
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. 유저네임 조회를 통한 사용자 객체 선언
        Optional<SiteUser> _siteUser =  this.userRepository.findByUsername(username);

        // 2. 비어있다면 UsernameNotFoundException 리턴
        if(_siteUser.isEmpty()){
            throw  new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        
        // 3. 사용자명이 admin 이면 ADMIN 권한 부여, 이외에는 USER 권한 부여
        // SimpleGrantedAuthority: 인가정보를 나타내는 클래스.  사용자 권한을 정의하고 관리
        if ("admin".equals(username)){
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else{
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }


        System.out.println("authorities = " + authorities);
        System.out.println("siteUser = " + siteUser);
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}