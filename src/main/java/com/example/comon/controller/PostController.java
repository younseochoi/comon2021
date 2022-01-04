package com.example.comon.controller;

import com.example.comon.DefaultResponse;
import com.example.comon.ResponseMessage;
import com.example.comon.StatusCode;
import com.example.comon.domain.entity.Post;
import com.example.comon.dto.PostDto;
import com.example.comon.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private PostService postService;

    @Autowired
    PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity getPost(@RequestParam(required = false) Long id) {
        try {
            if (id == null) {
                List<Post> posts = postService.findAll();
                return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST, posts), HttpStatus.MULTI_STATUS.OK);
            } else {
                PostDto post = postService.findById(id);
                if (post == null) {
                    return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.READ_POST, post), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostDto postDto) {
        Long id = postService.save(postDto);
        return new ResponseEntity(DefaultResponse.res(StatusCode.CREATED, ResponseMessage.POST_SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updatePost(@RequestParam(required = true) Long id, @RequestBody PostDto postDto) {
        if (postService.update(id, postDto) != null) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.UPDATE_POST, id), HttpStatus.OK);
        } else {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(required = true) Long id) {
        if(postService.delete(id) != 0) {
            return new ResponseEntity(DefaultResponse.res(StatusCode.OK, ResponseMessage.DELETE_POST, id), HttpStatus.OK);
        } else {
            return new ResponseEntity(DefaultResponse.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_POST), HttpStatus.OK);
        }
    }
}