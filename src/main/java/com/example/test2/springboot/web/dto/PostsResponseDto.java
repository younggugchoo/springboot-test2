package com.example.test2.springboot.web.dto;

import com.example.test2.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getAuthor();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
