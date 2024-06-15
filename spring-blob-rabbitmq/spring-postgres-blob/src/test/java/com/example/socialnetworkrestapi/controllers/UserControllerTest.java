package com.example.socialnetworkrestapi.controllers;

import com.example.socialnetworkrestapi.models.DTO.user.UserRegistrationDTO;
import com.example.socialnetworkrestapi.models.DTO.user.UserResponseDTO;
import com.example.socialnetworkrestapi.models.Role;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import com.example.socialnetworkrestapi.security.JwtCore;
import com.example.socialnetworkrestapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    private static UserRegistrationDTO userRegistrationDTO;
    private static UserResponseDTO userResponseDTO;
    private static UserEntity userEntity;


    @BeforeAll
    public static void setUp() {
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setName("user");
        userRegistrationDTO.setPassword("password");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("user");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("user");
        userEntity.setRole(Role.USER);
        userEntity.setPosts(new ArrayList<>());
    }

    @Test
    public void registerNewUserFormOkTest() throws Exception {
        this.mockMvc.perform(get("/user/new"))
                .andDo(print())
                .andExpect(view().name("user_register_form"));
    }

    @Test
    public void saveNewUserSeeOtherTest() throws Exception{
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        doNothing().when(userService).save(any());

        mockMvc.perform(post("/user/new")
                .flashAttr("user", userRegistrationDTO))
                .andDo(print())
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", "/user/login"))
                .andExpect(content().string("User successfully created, redirecting..."));

        verify(userService, times(1)).save(any());
    }

    @Test
    public void saveNewUserBadRequestTest() throws Exception{
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        doThrow(new DataIntegrityViolationException("User already exists")).when(userService).save(any());
        mockMvc.perform(post("/user/new")
                .flashAttr("user", userRegistrationDTO))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-Info", "Creating user failed"));

        verify(userService, times(1)).save(any());
    }

    @Test
    public void getRequestsUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/user/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void postRequestsUnauthorizedTest() throws Exception {
        this.mockMvc.perform(post("/user/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void editUserFormOkTest() throws Exception {
        when(userService.findDtoById((anyLong()))).thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(get("/user/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("user_edit_form"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userResponseDTO));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void saveEditedUserOkTest() throws Exception {
        when(userService.findDtoById(anyLong())).thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(post("/user/edit")
                        .with(csrf())
                        .flashAttr("user", userResponseDTO))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully edited"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void getUserByIdOrNameBadRequestTest() throws Exception {

        mockMvc.perform(get("/user/get"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("NullPointerException, user can't be find"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllUsersOkTest() throws Exception{
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(userResponseDTO));

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Info"))
                .andExpect(jsonPath("$[0].name").value("user"));

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void getAllUsersForbiddenTest() throws Exception{
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(userResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}