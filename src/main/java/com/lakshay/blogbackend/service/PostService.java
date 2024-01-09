package com.lakshay.blogbackend.service;

import com.lakshay.blogbackend.dto.PostDTO;
import com.lakshay.blogbackend.entity.Post;
import com.lakshay.blogbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Authenticate authenticate;

    public Post addPost(PostDTO postDTO) {
        authenticate.verifyUsername(postDTO.getUsername());
        Post post = Post.validate(null, postDTO.getTitle(), postDTO.getBody(), postDTO.getUsername());
        return postRepository.save(post);
    }
}
