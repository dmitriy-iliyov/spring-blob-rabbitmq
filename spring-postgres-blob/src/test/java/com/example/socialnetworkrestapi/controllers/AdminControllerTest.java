package com.example.socialnetworkrestapi.controllers;

import com.example.socialnetworkrestapi.models.DTO.user.AdminRegistrationDTO;
import com.example.socialnetworkrestapi.models.DTO.user.AdminResponseDTO;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import com.example.socialnetworkrestapi.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    private static AdminRegistrationDTO adminRegistrationDTO;
    private static AdminResponseDTO adminResponseDTO;
    private static UserEntity adminEntity;


    @BeforeAll
    public static void setUp() {
        adminRegistrationDTO = new AdminRegistrationDTO();
        adminRegistrationDTO.setName("admin");
        adminRegistrationDTO.setPassword("password");

        adminResponseDTO = new AdminResponseDTO();
        adminResponseDTO.setId(1L);
        adminResponseDTO.setName("admin");

        adminEntity = new UserEntity();
        adminEntity.setId(1L);
        adminEntity.setName("admin");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void registerNewAdminTest() throws Exception {
        mockMvc.perform(get("/admin/new"))
                .andDo(print())
                .andExpect(view().name("admin_register_form"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void saveNewAdminSeeOtherTest() throws Exception {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        doNothing().when(userService).save(any());

        mockMvc.perform(post("/admin/new")
                        .flashAttr("admin", adminRegistrationDTO))
                .andDo(print())
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", "/user/login"))
                .andExpect(content().string("Admin successfully created, redirecting..."));

        verify(userService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void saveNewAdminBadRequestTest() throws Exception {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        doThrow(new DataIntegrityViolationException("Admin already exists")).when(userService).save(any());

        mockMvc.perform(post("/admin/new")
                        .flashAttr("admin", adminRegistrationDTO))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAdminByIdOkTest() throws Exception{
        when(userService.findEntityById(anyLong())).thenReturn(Optional.of(adminEntity));

        mockMvc.perform(get("/admin/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"admin\",\"password\":null,\"role\":null}"));

        verify(userService, times(1)).findEntityById(anyLong());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAdminByIdNotFoundTest() throws Exception {
        when(userService.findEntityById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/get/1"))
                .andExpect(status().isNotFound())
                .andExpect(header().exists("X-Info"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllAdminsOkTest() throws Exception {
        when(userService.findAllAdmins()).thenReturn(Collections.singletonList(adminResponseDTO));

        mockMvc.perform(get("/admin/all"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Info"))
                .andExpect(jsonPath("$[0].name").value("admin"));

        verify(userService, times(1)).findAllAdmins();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteAdminNoContentTest() throws Exception {
        doNothing().when(userService).deleteByNameAndPassword(anyString(), anyString());
        when(passwordEncoder.encode(any())).thenReturn("password");

        mockMvc.perform(delete("/admin/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"admin\",\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("Admin with name admin has been successfully deleted"));

        verify(userService, times(1)).deleteByNameAndPassword(anyString(), anyString());
    }
}
