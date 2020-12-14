package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

// Stub
public class PostRepository {
    private static AtomicInteger postId = new AtomicInteger(0);
    ConcurrentMap<Long, Post> postRepository;

    public PostRepository() {
        this.postRepository = new ConcurrentHashMap<>();
    }

    public ConcurrentMap<Long, Post> all() {
        return postRepository;
    }

    public Optional<Post> getById(long id) {
        return Optional.of(postRepository.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();

        if (id == 0) {
            newPost(id, post);
        } else {
            if (postRepository.containsKey(id)) {
                replacePost(id, post);
            } else {
                newPost(id, post);
            }
        }
        return post;
    }

    public void newPost(long id, Post post) {
        id = postId.incrementAndGet();
        post.setId(id);
        postRepository.put(id, post);
    }

    public void replacePost (long id, Post post) {
      postRepository.replace(id, post);
    }

    public void removeById(long id) {
        postRepository.remove(id);
    }

}
