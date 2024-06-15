package com.example.services;

import com.example.models.DTO.post.PostCreatingDTO;
import com.example.models.DTO.post.PostResponseDTO;
import com.example.models.DTO.user.UserResponseDTO;
import com.example.models.entitys.CategoryEntity;
import com.example.models.entitys.PostEntity;
import com.example.models.entitys.UserEntity;
import com.example.repositorys.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;


    @Transactional
    public void save(PostCreatingDTO postCreatingDTO) throws ChangeSetPersister.NotFoundException {
        UserEntity userEntity = userService.findEntityById(postCreatingDTO.getUserID()).orElse(null);
        CategoryEntity categoryEntity = categoryService.findById(postCreatingDTO.getCategoryID()).orElse(null);
        if(userEntity != null && categoryEntity != null){
            postRepository.save(PostCreatingDTO.toEntity(postCreatingDTO));
        }
        else{
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Transactional
    public Optional<PostResponseDTO> findById(String id){
        return postRepository.findById(id).map(PostResponseDTO::toDTO);
    }

    @Transactional
    public List<PostResponseDTO> findAllByUserIdOrCategoryId(String userId, String categoryId){
        List<PostResponseDTO> postDTOS = new ArrayList<>();
        postRepository.findAllByUserIDOrCategoryID(userId, categoryId).forEach(postEntity -> postDTOS.add(PostResponseDTO.toDTO(postEntity)));
        return postDTOS;
    }

    @Transactional
    public List<PostResponseDTO> findAll(){
        List<PostResponseDTO> postDTOS = new ArrayList<>();
        postRepository.findAll().forEach(postEntity -> postDTOS.add(PostResponseDTO.toDTO(postEntity)));
        return postDTOS;
    }

    @Transactional
    public void deleteById(String id){
        postRepository.deleteById(id);
    }
}
