//package com.example.backend.govflow.service;
//
//import com.example.backend.govflow.dto.ChangePasswordDto;
//import com.example.backend.govflow.dto.RegisterRequest;
//import com.example.backend.govflow.dto.UpdateEmployeeDto; // ✅ 1. استيراد الـ DTO الجديد
//import com.example.backend.govflow.dto.UpdateProfileDto;
//import com.example.backend.govflow.dto.UserDto;
//import com.example.backend.govflow.entity.Department;
//import com.example.backend.govflow.entity.Role;
//import com.example.backend.govflow.entity.User;
//import com.example.backend.govflow.repository.DepartmentRepository;
//import com.example.backend.govflow.repository.RoleRepository;
//import com.example.backend.govflow.repository.UserRepository;
//import jakarta.persistence.EntityNotFoundException; // ✅ 2. استيراد الاستثناء المطلوب
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.List; // ✅ 3. استيراد إضافي
//import java.util.stream.Collectors; // ✅ 4. استيراد إضافي
//
//
//@Service
//public class UserService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final DepartmentRepository departmentRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public UserService(UserRepository userRepository, RoleRepository roleRepository, DepartmentRepository departmentRepository, @Lazy PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.departmentRepository = departmentRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public User register(RegisterRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new IllegalStateException("A user with this email already exists.");
//        }
//        if (userRepository.existsByNationalId(request.getNationalId())) {
//            throw new IllegalStateException("A user with this national ID already exists.");
//        }
//
//        String roleName = (StringUtils.hasText(request.getRole()))
//                ? request.getRole().toUpperCase()
//                : "CITIZEN";
//
//        Role userRole = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Error: Role '" + roleName + "' not found in database."));
//
//        User.UserBuilder userBuilder = User.builder()
//                .fullName(request.getFullName())
//                .nationalId(request.getNationalId())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(userRole)
//                .phoneNumber(request.getPhoneNumber())
//                .address(request.getAddress())
//                .jobRoleCode(request.getJobRoleCode());
//
//        if (!roleName.equals("CITIZEN")) {
//            Department defaultDepartment = departmentRepository.findById(1L)
//                    .orElseThrow(() -> new RuntimeException("Error: Default department with ID 1 not found."));
//            userBuilder.department(defaultDepartment);
//        }
//
//        User user = userBuilder.build();
//        return userRepository.save(user);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByNationalId(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with national ID: " + username));
//    }
//
//    public UserDto updateProfile(Long userId, UpdateProfileDto dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
//
//        user.setFullName(dto.getFullName());
//        user.setEmail(dto.getEmail());
//        user.setPhoneNumber(dto.getPhoneNumber());
//        user.setAddress(dto.getAddress());
//
//        User updatedUser = userRepository.save(user);
//
//        return new UserDto(updatedUser.getId(), updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getRole().getName());
//    }
//
//    public void changePassword(Long userId, ChangePasswordDto dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
//
//        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Incorrect current password.");
//        }
//
//        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
//        userRepository.save(user);
//    }
//
//    // ==================================================================
//    // ✅ ===== START: الدالة الجديدة التي تمت إضافتها للتحديث ===== ✅
//    // ==================================================================
//
//    /**
//     * Updates an employee's details. This action is typically performed by a manager.
//     * @param userId The ID of the user to update.
//     * @param dto The data transfer object containing the new information.
//     * @return A UserDto representing the updated user.
//     */
//    public UserDto updateEmployee(Long userId, UpdateEmployeeDto dto) {
//        // 1. ابحث عن المستخدم المراد تحديثه
//        User userToUpdate = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
//
//        // 2. ابحث عن الدور الجديد والقسم الجديد من قاعدة البيانات
//        Role newRole = roleRepository.findById(dto.getRoleId())
//                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + dto.getRoleId()));
//
//        Department newDepartment = departmentRepository.findById(dto.getDepartmentId())
//                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));
//
//        // 3. قم بتحديث بيانات المستخدم
//        userToUpdate.setFullName(dto.getFullName());
//        userToUpdate.setEmail(dto.getEmail());
//        userToUpdate.setRole(newRole);
//        userToUpdate.setDepartment(newDepartment);
//
//        // 4. احفظ المستخدم المحدث في قاعدة البيانات
//        User updatedUser = userRepository.save(userToUpdate);
//
//        // 5. قم بإرجاع DTO يحتوي على البيانات المحدثة
//        return new UserDto(updatedUser.getId(), updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getRole().getName());
//    }
//
//    public List<UserDto> findAllEmployees() {
//        return userRepository.findAllByRoleNameNot("CITIZEN")
//                .stream()
//                .map(user -> new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName()))
//                .collect(Collectors.toList());
//    }
//
//    public void deleteUser(Long userId) {
//        if (!userRepository.existsById(userId)) {
//            throw new EntityNotFoundException("User not found with id: " + userId);
//        }
//        userRepository.deleteById(userId);
//    }
//    // ==================================================================
//    // ✅ ===== END: الدالة الجديدة التي تمت إضافتها ===== ✅
//    // ==================================================================
//}




package com.example.backend.govflow.service;

import com.example.backend.govflow.dto.ChangePasswordDto;
import com.example.backend.govflow.dto.RegisterRequest;
import com.example.backend.govflow.dto.UpdateEmployeeDto;
import com.example.backend.govflow.dto.UpdateProfileDto;
import com.example.backend.govflow.dto.UserDto;
import com.example.backend.govflow.entity.Department;
import com.example.backend.govflow.entity.Role;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.repository.DepartmentRepository;
import com.example.backend.govflow.repository.RoleRepository;
import com.example.backend.govflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, DepartmentRepository departmentRepository, @Lazy PasswordEncoder passwordEncoder, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("A user with this email already exists.");
        }
        if (userRepository.existsByNationalId(request.getNationalId())) {
            throw new IllegalStateException("A user with this national ID already exists.");
        }

        String roleName = (StringUtils.hasText(request.getRole()))
                ? request.getRole().toUpperCase()
                : "CITIZEN";

        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role '" + roleName + "' not found in database."));

        User.UserBuilder userBuilder = User.builder()
                .fullName(request.getFullName())
                .nationalId(request.getNationalId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .jobRoleCode(request.getJobRoleCode());

        if (!roleName.equals("CITIZEN")) {
            Department defaultDepartment = departmentRepository.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Error: Default department with ID 1 not found."));
            userBuilder.department(defaultDepartment);
        }

        User user = userBuilder.build();
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByNationalId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with national ID: " + username));
    }

    public UserDto updateProfile(Long userId, UpdateProfileDto dto, MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeFile(avatarFile);
            user.setAvatar(avatarUrl);
        }

        User updatedUser = userRepository.save(user);

        return new UserDto(updatedUser.getId(), updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getRole().getName(), updatedUser.getAvatar());
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void changePassword(Long userId, ChangePasswordDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect current password.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public UserDto updateEmployee(Long userId, UpdateEmployeeDto dto) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Role newRole = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + dto.getRoleId()));

        Department newDepartment = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        userToUpdate.setFullName(dto.getFullName());
        userToUpdate.setEmail(dto.getEmail());
        userToUpdate.setRole(newRole);
        userToUpdate.setDepartment(newDepartment);

        User updatedUser = userRepository.save(userToUpdate);

        return new UserDto(updatedUser.getId(), updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getRole().getName());
    }

    public List<UserDto> findAllEmployees() {
        return userRepository.findAllByRoleNameNot("CITIZEN")
                .stream()
                .map(user -> new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName()))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}