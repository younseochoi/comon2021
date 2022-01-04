package com.example.comon.service;

import com.example.comon.domain.entity.Post;
import com.example.comon.domain.repository.PostRepository;
import com.example.comon.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional
    public PostDto findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        // 찾는 게시글이 있으면
        if(postOptional.isPresent()) {
            Post post = postOptional.get();
            // PostDto로 반환
            return PostDto.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .author(post.getAuthor())
                    .createdDate(post.getCreatedDate())
                    .modifiedDate(post.getModifiedDate())
                    .readCount(post.getReadCount())
                    .categoryId(post.getCategoryId())
                    .build();
        } else {
            // 없으면 null 반환
            return null;
        }
    }

    @Transactional
    public Long save(PostDto postDto) {
        return postRepository.save(postDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostDto postDto) {
        // 찾는 게시물 있으면
        if(postRepository.findById(id).isPresent()) {
            postDto.setId(id);
            return save(postDto);
        } else {
            return null;
        }
    }

    @Transactional
    public Long delete(Long id) {
        if(postRepository.findById(id).isPresent()) {
            postRepository.deleteById(id);
            return id;
        } else {
            return 0L;
        }
    }
}
