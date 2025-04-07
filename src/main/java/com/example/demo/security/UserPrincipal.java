//package com.example.demo.security;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//public class UserPrincipal implements UserDetails {
//
//    private String id;
//    private String username;
//    private String email;
//    private String password;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public UserPrincipal(String id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.authorities = authorities;
//    }
//
//    public static UserPrincipal create(Object user, String role) {
//        // This can handle both student and instructor
//        String id;
//        String username;
//        String email;
//        String password;
//
//        if (user instanceof com.example.demo.dto.StudentDto) {
//            com.example.demo.dto.StudentDto student = (com.example.demo.dto.StudentDto) user;
//            id = student.getId();
//            username = student.getUsername();
//            email = student.getEmail();
//            password = student.getPassword();
//        } else if (user instanceof com.example.demo.dto.InstructorDto) {
//            com.example.demo.dto.InstructorDto instructor = (com.example.demo.dto.InstructorDto) user;
//            id = instructor.getId();
//            username = instructor.getUsername();
//            email = instructor.getEmail();
//            password = instructor.getPassword();
//        } else {
//            throw new IllegalArgumentException("Unsupported user type");
//        }
//
//        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
//
//        return new UserPrincipal(id, username, email, password, authorities);
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}