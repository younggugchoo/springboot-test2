package com.example.test2.springboot.web;

import com.example.test2.springboot.config.auth.LoginUser;
import com.example.test2.springboot.config.auth.dto.SessionUser;
import com.example.test2.springboot.service.posts.PostsService;
import com.example.test2.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    @GetMapping("/")
    /*
    * 어떤 컨트롤러에서 @LoginUser를 참조하면 세션을 가져올수 있음.
    * */
    public String index(Model model, @LoginUser SessionUser user){
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/index2")
    public String Test(){
        return "index2";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }



}
