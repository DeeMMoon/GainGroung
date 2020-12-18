package com.gainground.gainGroung.controller;

import com.gainground.gainGroung.entity.Post;
import com.gainground.gainGroung.entity.ProfileEmpl;
import com.gainground.gainGroung.entity.User;
import com.gainground.gainGroung.repository.PostRepository;
import com.gainground.gainGroung.repository.ProfileRepository;
import com.gainground.gainGroung.repository.UserRepository;
import com.gainground.gainGroung.service.PostService;
import com.gainground.gainGroung.service.ProfileService;
import com.gainground.gainGroung.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class RatingController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/rating")
    public String userList(Model model) {
        model.addAttribute("allProfiles", profileService.allProfiles());
        return "rating";
    }
    @GetMapping("/rating/{profile_id}")
    public String blogDetail(@PathVariable(value = "profile_id") long profId, Model model){
        if(!profileRepository.existsById(profId)){
            return "redirect:/rating";
        }
        ProfileEmpl profileEmpl = profileRepository.findProfileEmplById(profId);
        model.addAttribute("profile",profileEmpl);
        return "profile-rating";
    }
    @GetMapping("/rating/blog/{profile_id}")
    public String ratingBlog(@PathVariable(value = "profile_id") long profId, Model model, User user){
        ProfileEmpl profileEmpl = profileRepository.findProfileEmplById(profId);
        user=userRepository.findByProfileEmpl(profileEmpl);
        model.addAttribute("posts",postService.authorPosts(user.getId()));
        return "post-rating";
    }
    @GetMapping("/rating/blog/posts/{el_id}")
    public String ratingBlogPost(@PathVariable(value = "el_id") long postId, Model model, User user){
        if(!postRepository.existsById(postId)){
            return "redirect:/rating/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post",res);
        return "rating-post-details";
    }
}