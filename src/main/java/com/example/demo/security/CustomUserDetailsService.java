//package com.example.demo.security;
//
//import com.example.demo.dto.InstructorDto;
//import com.example.demo.dto.StudentDto;
//import com.example.demo.service.InstructorService;
//import com.example.demo.service.StudentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private StudentService studentService;
//
//    @Autowired
//    private InstructorService instructorService;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        // Try to find the user as a student first
//        try {
//            StudentDto student = studentService.findStudentByEmail(email);
//            return UserPrincipal.create(student, "student");
//        } catch (Exception e) {
//            // If not found as student, try as instructor
//            try {
//                InstructorDto instructor = instructorService.findInstructorByEmail(email);
//                return UserPrincipal.create(instructor, "instructor");
//            } catch (Exception ex) {
//                throw new UsernameNotFoundException("User not found with email : " + email);
//            }
//        }
//    }
//
//    // This method is used by JWTAuthenticationFilter
//    public UserDetails loadUserById(String id, String role) {
//        if ("ROLE_STUDENT".equals(role)) {
//            StudentDto student = studentService.findStudentById(id);
//            return UserPrincipal.create(student, "student");
//        } else if ("ROLE_INSTRUCTOR".equals(role)) {
//            InstructorDto instructor = instructorService.findInstructorById(id);
//            return UserPrincipal.create(instructor, "instructor");
//        }
//        throw new UsernameNotFoundException("User not found with id : " + id);
//    }
//}
