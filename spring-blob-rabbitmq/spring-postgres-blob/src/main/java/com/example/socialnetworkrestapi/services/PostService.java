package com.example.socialnetworkrestapi.services;

import com.example.socialnetworkrestapi.models.DTO.post.PostCreatingDTO;
import com.example.socialnetworkrestapi.models.DTO.post.PostResponseDTO;
import com.example.socialnetworkrestapi.models.entitys.CategoryEntity;
import com.example.socialnetworkrestapi.models.entitys.UserEntity;
import com.example.socialnetworkrestapi.repositorys.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    public void save(PostCreatingDTO post) throws ChangeSetPersister.NotFoundException {
        UserEntity userEntity = userService.findEntityById(post.getUserID()).orElse(null);
        CategoryEntity categoryEntity = categoryService.findById(post.getCategoryID()).orElse(null);
        if(userEntity != null && categoryEntity != null){
            postRepository.save(PostCreatingDTO.toEntity(post, userEntity, categoryEntity));
        }
        else{
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @Transactional
    public Optional<PostResponseDTO> findById(Long id){
        return postRepository.findById(id).map(PostResponseDTO::toDTO);
    }

    @Transactional
    public List<PostResponseDTO> findAllByUserIdOrUserNameOrCategoryIdOrCategoryName(Long userId, String userName, Long categoryId, String categoryName){
        List<PostResponseDTO> postDTOS = new ArrayList<>();
        postRepository.findAllByUserIdOrUserNameOrCategoryIdOrCategoryName(userId, userName, categoryId, categoryName).forEach(postEntity -> postDTOS.add(PostResponseDTO.toDTO(postEntity)));
        return postDTOS;
    }

    @Transactional
    public List<PostResponseDTO> findAll(){
        List<PostResponseDTO> postDTOS = new ArrayList<>();
        postRepository.findAll().forEach(postEntity -> postDTOS.add(PostResponseDTO.toDTO(postEntity)));
        return postDTOS;
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
}
