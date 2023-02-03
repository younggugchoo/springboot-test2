package com.example.test2.springboot.service.posts;

import com.example.test2.springboot.domain.posts.Posts;
import com.example.test2.springboot.domain.posts.PostsRepository;
import com.example.test2.springboot.web.dto.PostsResponseDto;
import com.example.test2.springboot.web.dto.PostsSaveRequestDto;
import com.example.test2.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts =  postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다.id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다.id=" + id));

        return new PostsResponseDto(entity);
    }

    public List<PostsResponseDto> findAll(){
        List<Posts> entities = postsRepository.findAll();

        List<PostsResponseDto> lists = new ArrayList<>();

        for(Posts item :entities) {
            lists.add(new PostsResponseDto(item));
        }

        return lists;

    }


}
