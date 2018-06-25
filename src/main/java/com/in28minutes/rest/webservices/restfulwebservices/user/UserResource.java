package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserResource {

    @Autowired
    private UserDaoService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/jpa/users/{userId}/posts")
    public List<Post> retrieveAllUserPosts(@PathVariable int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) throw new UserNotFoundException("id-"+userId);
        return userOptional.get().getPosts();
    }

    @GetMapping("/users/{userId}/posts/{id}")
    public Post getPostDetails(@PathVariable int userId, @PathVariable int id) {
        return service.findSinglePost(userId, id);
    }

    @GetMapping("/jpa/users/{id}")
    public Resource<User> retrieveUser(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException(String.format("id - %s", id));
        // HATEOAS
        // all-users, SERVER_PATH+"/users"
        //retrieveAllUsers
        Resource<User> resource = new Resource<>(user.get());
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));
        return resource;
    }

    @DeleteMapping("/jpa/users/{id}")
    public ResponseEntity deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/jpa/users/{userId}/posts")
    public ResponseEntity<Object> createUserPost(@PathVariable int userId, @RequestBody Post post) {
        User savedUser = service.findOne(userId);
        post = service.savePost(savedUser, post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(post.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
