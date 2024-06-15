package com.example.socialnetworkrestapi.controllers;

import com.example.socialnetworkrestapi.models.DTO.category.CategoryResponseDTO;
import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import com.example.socialnetworkrestapi.services.CategoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    private static CategoryResponseDTO categoryResponseDTO;


    @BeforeAll
    public static void setUp() {
        categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(1L);
        categoryResponseDTO.setName("categoryName");
    }

    @Test
    public void postRequestsUnauthorizedTest() throws Exception {
        this.mockMvc.perform(post("/category/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getRequestsUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/category/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void addNewCategoryFormOkTest() throws Exception {
        mockMvc.perform(get("/category/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("category_register_form"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void saveNewCategoryCreatedTest() throws Exception {
        doNothing().when(categoryService).save(any());

        mockMvc.perform(post("/category/new")
                        .param("name", "newCategory"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("X-info", "Creating category"))
                .andExpect(content().string("Category successfully created"));

        verify(categoryService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void saveNewCategoryCreatedAgainstTest() throws Exception {
        doThrow(new DataIntegrityViolationException("Category with this name already exists"))
                .when(categoryService).save(any());

        mockMvc.perform(post("/category/new")
                        .param("name", "existingCategory"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("X-info", "Creating category"))
                .andExpect(content().string("Category with this name existingCategory is exist"));

        verify(categoryService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void getCategoryByIdOrNameOkTest() throws Exception {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("category");

        when(categoryService.findById(anyLong())).thenReturn(Optional.of(categoryEntity));

        mockMvc.perform(get("/category/get").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting category"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("category"));

        when(categoryService.findByName(anyString())).thenReturn(Optional.of(categoryEntity));

        mockMvc.perform(get("/category/get").param("name", "category"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting category"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("category"));

        verify(categoryService, times(1)).findById(anyLong());
        verify(categoryService, times(1)).findByName(anyString());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void getCategoryByIdOrNameNotFoundTest() throws Exception {
        when(categoryService.findById(anyLong())).thenReturn(Optional.empty());
        when(categoryService.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/category/get").param("id", "1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting category"));

        mockMvc.perform(get("/category/get").param("name", "unknown"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting category"));

        verify(categoryService, times(1)).findById(anyLong());
        verify(categoryService, times(1)).findByName(anyString());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void getCategoryByIdOrNameBadRequestTest() throws Exception {
        mockMvc.perform(get("/category/get"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("X-info", "Getting category"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllCategoriesOkTest() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.singletonList(categoryResponseDTO));

        mockMvc.perform(get("/category/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting all category"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("categoryName"));

        verify(categoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllCategoriesNotFoundTest() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/category/all"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting all category"));

        verify(categoryService, times(1)).findAll();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteCategoryNoContentTest() throws Exception {
        doNothing().when(categoryService).deleteById(anyLong());

        mockMvc.perform(delete("/category/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string("X-info", "Deleting category"))
                .andExpect(content().string("Category with id 1 has been successfully deleted"));

        verify(categoryService, times(1)).deleteById(anyLong());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteCategoryInternalServerErrorTest() throws Exception {
        doThrow(new RuntimeException("Failed to delete category")).when(categoryService).deleteById(anyLong());

        mockMvc.perform(delete("/category/delete/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("X-info", "Deleting category"))
                .andExpect(content().string("Failed to delete admin with id 1"));

        verify(categoryService, times(1)).deleteById(anyLong());
    }
}
