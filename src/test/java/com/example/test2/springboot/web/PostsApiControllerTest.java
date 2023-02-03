package com.example.test2.springboot.web;

import com.example.test2.springboot.domain.posts.Posts;
import com.example.test2.springboot.domain.posts.PostsRepository;
import com.example.test2.springboot.web.dto.PostsSaveRequestDto;
import com.example.test2.springboot.web.dto.PostsUpdateRequestDto;
import org.hibernate.loader.plan.build.internal.LoadGraphLoadPlanBuildingStrategy;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/*
    JPA 테스트를 위해
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    TestRestTemplate
    사용함.

 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void RegistryPosts() throws Exception{
        String title = "title";
        String content = "content";
        String author = "author";

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, postsSaveRequestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void UdpatePosts() throws  Exception{
        //데이터 저장
        Posts posts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        Long updateId = posts.getId();
        String changedTitle = "title_modified";
        String changedContent = "content_modified";

        //변경될 정보
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(changedTitle).content(changedContent).build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Posts> all = postsRepository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(changedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(changedContent);
    }

    @Test
    public void Registry_BaseTime(){
        LocalDateTime now = LocalDateTime.of(2019, 6,4,0,0,0);

        postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

        List<Posts> all = postsRepository.findAll();

        Posts posts = all.get(0);

        System.out.println(">>>>>>>>>>>>CreateDate=" + posts.getCreateDate()+",modifiedDate=" + posts.getModifiedDate());

        assertThat(posts.getCreateDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);

    }
}
