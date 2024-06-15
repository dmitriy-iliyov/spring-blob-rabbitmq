package com.example.socialnetworkrestapi.controllers;

import com.example.socialnetworkrestapi.models.DTO.user.AdminResponseDTO;
import com.example.socialnetworkrestapi.models.DTO.user.UserLogInDTO;
import com.example.socialnetworkrestapi.models.DTO.user.UserRegistrationDTO;
import com.example.socialnetworkrestapi.models.DTO.user.UserResponseDTO;
import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import com.example.socialnetworkrestapi.security.JwtCore;
import com.example.socialnetworkrestapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String registerNewUserForm(Model model){
        model.addAttribute("user", new UserRegistrationDTO());
        return "user_register_form";
    }

    @PostMapping("/new")
    public ResponseEntity<String> saveNewUser(@ModelAttribute("user") @Valid UserRegistrationDTO user,
                                         BindingResult bindingResult){

        HttpHeaders httpHeaders = new HttpHeaders();

        if(bindingResult.hasErrors()){
            httpHeaders.add("X-Info", "Invalid user data");
            httpHeaders.setLocation(URI.create("/user/login"));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(httpHeaders)
                    .body("Invalid data, redirecting...");
        }
        try{
            userService.save(user, passwordEncoder);
            httpHeaders.add("X-Info", "Creating user");
            httpHeaders.setLocation(URI.create("/user/login"));
            return ResponseEntity
                    .status(HttpStatus.SEE_OTHER)
                    .headers(httpHeaders)
                    .body("User successfully created, redirecting...");
        }catch (DataIntegrityViolationException e){
            httpHeaders.add("X-Info", "Creating user failed");
            System.out.println("EXCEPTION  " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .headers(httpHeaders)
                    .body("User with this name already exists");
        }
    }

    @GetMapping("/login")
    public String loggingUserForm(Model model){
        model.addAttribute("user", new UserLogInDTO());
        return "user_signing_in_form";
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@ModelAttribute("user") UserLogInDTO user){

        Authentication authentication;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Authenticate user");

        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        } catch (BadCredentialsException e){
            System.out.println("EXCEPTION  " + e.getMessage());
            httpHeaders.add("Error-Message", "Incorrect password, " + e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .headers(httpHeaders)
                    .body("Incorrect password or user with " + user.getName() + " name  isn't exist.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(httpHeaders)
                .body(jwt);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String editUserForm(@PathVariable Long id, Model model){
        UserResponseDTO userResponseDTO = userService.findDtoById(id).orElse(null);
        if(userResponseDTO != null){
            model.addAttribute("user", userResponseDTO);
            return "user_edit_form";
        }
        else{
            System.out.println("EXCEPTION  User not found in database!");
            return "redirect:/user/login";
        }
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> saveEditedUser(@ModelAttribute("user") UserResponseDTO userResponseDTO){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-info", "Editing user");

        System.out.println(userResponseDTO);

        try {
            userService.update(userResponseDTO, passwordEncoder);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body("Successfully edited");
        } catch (DataIntegrityViolationException e){
            System.out.println("EXCEPTION  " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body("failed(");
        }
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> getUserByIdOrName(HttpServletRequest request) {
        Long id = null;
        String name = null;
        String jwt = jwtCore.getTokenFromHttpHeader(request.getHeader("Authorization"));
        HttpHeaders httpHeaders = new HttpHeaders();

        if(jwt != null){
            name = jwtCore.getNameFromJwt(jwt);
            id = jwtCore.getIdFromJwt(jwt);
        }
        try {
            if(name != null){
                httpHeaders.add("X-Info", "Getting user by name");
                return getResponseEntity(httpHeaders, userService.findEntityByName(name));
            }
            if(id != null){
                httpHeaders.add("X-Info", "Getting user by id");
                return getResponseEntity(httpHeaders, userService.findEntityById(id));
            }
        } catch (NullPointerException e){
            System.out.println("EXCEPTION  " + e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(httpHeaders)
                .body("NullPointerException, user can't be find");
    }

    private ResponseEntity<?> getResponseEntity(HttpHeaders httpHeaders, Optional<UserEntity> userEntity) {
        UserResponseDTO userResponseDTO = userEntity.map(UserResponseDTO::toDTO).orElse(null);
        if(userResponseDTO != null){
            if(userResponseDTO.getRole() == Role.ADMIN)
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(httpHeaders)
                        .body(userEntity.map(AdminResponseDTO::toDTO));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body(userResponseDTO);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(httpHeaders)
                .body(null);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> users = userService.findAllUsers();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Info", "Getting all users");

        return users.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(null)
                : ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(users);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {

        String jwt = jwtCore.getTokenFromHttpHeader(request.getHeader("Authorization"));
        Long id = jwtCore.getIdFromJwt(jwt);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Info", "Deleting user by id");

        try {
            userService.deleteById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(httpHeaders)
                    .body("User with id " + id + " has been successfully deleted");
        } catch (Exception e) {
            System.out.println("EXCEPTION  " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(httpHeaders)
                    .body("Failed to delete user with id " + id + ": " + e.getMessage());
        }
    }
}
