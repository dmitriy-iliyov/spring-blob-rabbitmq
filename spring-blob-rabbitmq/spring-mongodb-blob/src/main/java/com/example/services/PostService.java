package com.example.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.models.DTO.MessageCreatingDTO;
import com.example.models.DTO.post.PostCreatingDTO;
import com.example.models.DTO.post.PostResponseDTO;
import com.example.models.entitys.CategoryEntity;
import com.example.models.entitys.UserEntity;
import com.example.rabbitmq.RabbitMQProducer;
import com.example.repositorys.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RabbitMQProducer rabbitMQProducer;

    @Value("${spring.cloud.azure.storage.blob.connection-string")
    private String connectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;
    private BlobServiceClient blobServiceClient;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService,
                       CategoryService categoryService, RabbitMQProducer rabbitMQProducer){
        this.postRepository = postRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostConstruct
    public void init() {
        blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
    }

    @Transactional
    public void save(PostCreatingDTO postCreatingDTO, MultipartFile imageFile)
            throws IOException, ChangeSetPersister.NotFoundException {

        UserEntity userEntity = userService.findEntityById(postCreatingDTO.getUserId()).orElse(null);
        CategoryEntity categoryEntity = categoryService.findById(postCreatingDTO.getCategoryId()).orElse(null);

        if(userEntity != null && categoryEntity != null){

            String blobFileName = imageFile.getOriginalFilename();
            BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(blobFileName);
            blobClient.upload(imageFile.getInputStream(), imageFile.getSize(), true);
            Instant uploadTime = Instant.now();

            String imageURI = blobClient.getBlobUrl();
            postCreatingDTO.setImageURI(imageURI);

            postRepository.save(PostCreatingDTO.toEntity(postCreatingDTO));

            rabbitMQProducer.sendMessageToMongoQueue(new MessageCreatingDTO(imageURI, uploadTime));
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
        postRepository.findAllByUserIdOrCategoryId(userId, categoryId).forEach(postEntity -> postDTOS.add(PostResponseDTO.toDTO(postEntity)));
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
