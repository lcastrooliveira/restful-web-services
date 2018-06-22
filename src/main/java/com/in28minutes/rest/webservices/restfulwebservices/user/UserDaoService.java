package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.in28minutes.rest.webservices.restfulwebservices.post.Post;
import com.in28minutes.rest.webservices.restfulwebservices.post.PostNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();
    private static int userCount = 3;
    private static int postCount = 3;

    static {
        users.add(new User(1, "Adam", new Date()));
        users.add(new User(2, "Eve", new Date()));
        users.add(new User(3, "Jack", new Date()));
        users.get(0).setPosts(Arrays.asList(new Post(1, "This is my first post ever", users.get(0)),
                                            new Post(2, "This is my second post", users.get(0))));
        users.get(2).setPosts(Arrays.asList(new Post(3, "I sell jackets", users.get(2))));
    }

    public List<User> findAll() {
        return users;
    }

    public List<Post> findPostsByUserId(Integer userId) {
        User user = findOne(userId);
        return user.getPosts();
    }

    public Post findSinglePost(int userId, int postId) {
        User user = findOne(userId);
        for(Post post: user.getPosts()) {
            if(post.getId() == postId)
                return post;
        }
        throw new PostNotFoundException("id-"+ postId);
    }

    public Post savePost(User user, Post post) {
        if(post.getId() == null) {
            post.setId(++postCount);
        }
        User foundUser = findOne(user.getId());
        foundUser.getPosts().add(post);
        return post;
    }

    public User save(User user) {
        if(user.getId() == null) {
            user.setId(++userCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for(User user:users) {
            if(user.getId()==id) {
                return user;
            }
        }
        throw new UserNotFoundException("id-" + id);
    }

    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()) {
            User user = iterator.next();
            if(user.getId() == id) {
                iterator.remove();
                return user;
            }
        }
        throw new UserNotFoundException("id-" + id);
    }
}
