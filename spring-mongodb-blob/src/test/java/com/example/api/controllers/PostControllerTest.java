package com.example.api.controllers;

import com.example.models.DTO.post.PostCreatingDTO;
import com.example.models.DTO.post.PostResponseDTO;
import com.example.models.entitys.CategoryEntity;
import com.example.models.entitys.PostEntity;
import com.example.models.entitys.UserEntity;
import com.example.services.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;
    private static PostCreatingDTO postCreatingDTO;
    private static PostResponseDTO postResponseDTO;
    private static PostEntity postEntity;

    @BeforeAll
    public static void setup(){
        postCreatingDTO = new PostCreatingDTO();
        postCreatingDTO.setUserID("507f1f77bcf86cd799439011");
        postCreatingDTO.setCategoryID("507f1f77bcf86cd799439011");
        postCreatingDTO.setTopic("topic");
        postCreatingDTO.setDescription("description");

        postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId("507f1f77bcf86cd799439011");
        postResponseDTO.setUserID("507f1f77bcf86cd799439011");
        postResponseDTO.setCategoryID("507f1f77bcf86cd799439011");
        postResponseDTO.setTopic("topic");
        postResponseDTO.setDescription("description");

        postEntity =  new PostEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("507f1f77bcf86cd799439011");
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId("507f1f77bcf86cd799439011");
        postEntity.setId("507f1f77bcf86cd799439011");
        postEntity.setUserID("507f1f77bcf86cd799439011");
        postEntity.setCategoryID("507f1f77bcf86cd799439011");
        postEntity.setTopic("topic");
        postEntity.setDescription("description");
    }

    @Test
    public void getRequestUnauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/post/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void postRequestUnauthorizedTest() throws Exception {
        this.mockMvc.perform(post("/post/**"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void addNewPostFormOkTest() throws Exception{
        mockMvc.perform(get("/post/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("post_register_form"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void saveNewPostCreatedTest() throws Exception {
        doNothing().when(postService).save(any());

        mockMvc.perform(post("/post/new")
                        .flashAttr("post", postCreatingDTO))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("X-info", "Creating post"))
                .andExpect(content().string("Post successfully created"));

        verify(postService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void saveNewPostNotFoundTest() throws Exception {
        doThrow(new ChangeSetPersister.NotFoundException())
                .when(postService).save(any());

        mockMvc.perform(post("/post/new")
                        .flashAttr("post", postCreatingDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Creating post"))
                .andExpect(content().string("User or category doesn't exist"));

        verify(postService, times(1)).save(any());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void getPostByIdOkTest() throws Exception{
        when(postService.findById(anyString())).thenReturn(Optional.of(postResponseDTO));

        mockMvc.perform(get("/post/get/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-Info", "Getting post by id"));

        verify(postService, times(1)).findById(anyString());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void getPostByIdNotFoundTest() throws Exception{
        when(postService.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/post/get/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Info", "Getting post by id"));

        verify(postService, times(1)).findById(anyString());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void findAllByUserIdOrCategoryIdOkTest() throws Exception{

        when(postService.findAllByUserIdOrCategoryId(any(), any()))
                .thenReturn(List.of(postResponseDTO, postResponseDTO));

        mockMvc.perform(get("/post/get").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting all post by user or category id or name"));

        mockMvc.perform(get("/post/get").param("categoryId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting all post by user or category id or name"));

        verify(postService, times(2)).findAllByUserIdOrCategoryId(any(), any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void findAllByUserIdOrCategoryIdNotFoundTest() throws Exception{
        when(postService.findAllByUserIdOrCategoryId(any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/post/get").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting all post by user or category id or name"));

        mockMvc.perform(get("/post/get").param("categoryId", "1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting all post by user or category id or name"));

        verify(postService, times(2)).findAllByUserIdOrCategoryId(any(), any());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void getAllPostsOkTest() throws Exception{
        when(postService.findAll()).thenReturn(List.of(postResponseDTO));

        mockMvc.perform(get("/post/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("X-info", "Getting all posts"));

        verify(postService, times(1)).findAll();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void getAllPostsNotFoundTest() throws Exception{
        when(postService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/post/all"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-info", "Getting all posts"));

        verify(postService, times(1)).findAll();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void deletePostNoContentTest() throws Exception {
        doNothing().when(postService).deleteById(anyString());

        mockMvc.perform(delete("/post/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(header().string("X-info", "Deleting post by id"))
                .andExpect(content().string("Post with id 1 has been successfully deleted"));

        verify(postService, times(1)).deleteById(anyString());
        }

    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    public void deletePostInternalServerErrorTest() throws Exception {
        doThrow(new IllegalArgumentException()).when(postService).deleteById(anyString());

        mockMvc.perform(delete("/post/delete/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("X-info", "Deleting post by id"))
                .andExpect(content().string("Failed to delete post with id 1"));

        verify(postService, times(1)).deleteById(anyString());
    }
}