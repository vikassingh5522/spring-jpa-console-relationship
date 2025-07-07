package com.example.service;

import com.example.entity.User;
import com.example.entity.UserProfile;
import com.example.entity.Post;
import com.example.entity.Role;
import com.example.entity.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    // User methods
    public User createUser(String username, String email) {
        User user = new User(username, email);
        entityManager.persist(user);
        return user;
    }

    public User createUserWithProfile(String username, String email,
                                      String firstName, String lastName,
                                      String phoneNumber, String bio) {
        User user = new User(username, email);
        UserProfile profile = new UserProfile(firstName, lastName, phoneNumber, bio);
        profile.setUser(user);
        user.setProfile(profile);
        entityManager.persist(user);
        entityManager.persist(profile);
        return user;
    }

    public User addAddressToUser(Long userId, String street, String city, String state, String zipCode) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            Address address = new Address(street, city, state, zipCode);
            user.setAddress(address);
            entityManager.merge(user);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        List<User> users = query.getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public void updateUserEmail(Long userId, String newEmail) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            user.setEmail(newEmail);
            entityManager.merge(user);
        }
    }

    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    // Post methods
    public Post createPost(Long userId, String title, String content) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            Post post = new Post(title, content, user);
            entityManager.persist(post);
            return post;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPostById(Long id) {
        Post post = entityManager.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllPosts() {
        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p", Post.class);
        return query.getResultList();
    }

    public void updatePost(Long postId, String newTitle, String newContent) {
        Post post = entityManager.find(Post.class, postId);
        if (post != null) {
            post.setTitle(newTitle);
            post.setContent(newContent);
            entityManager.merge(post);
        }
    }

    public void deletePost(Long id) {
        Post post = entityManager.find(Post.class, id);
        if (post != null) {
            entityManager.remove(post);
        }
    }

    @Transactional(readOnly = true)
    public List<Post> findPostsByUser(Long userId) {
        TypedQuery<Post> query = entityManager.createQuery(
                "SELECT p FROM Post p WHERE p.author.id = :userId ORDER BY p.createdAt DESC",
                Post.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // Role methods
    public Role createRole(String name, String description) {
        Role role = new Role(name, description);
        entityManager.persist(role);
        return role;
    }

    @Transactional(readOnly = true)
    public Optional<Role> findRoleById(Long id) {
        Role role = entityManager.find(Role.class, id);
        return Optional.ofNullable(role);
    }

    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    public void updateRole(Long roleId, String newName, String newDescription) {
        Role role = entityManager.find(Role.class, roleId);
        if (role != null) {
            role.setName(newName);
            role.setDescription(newDescription);
            entityManager.merge(role);
        }
    }

    public void deleteRole(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        User user = entityManager.find(User.class, userId);
        Role role = entityManager.find(Role.class, roleId);
        if (user != null && role != null) {
            user.addRole(role);
            entityManager.merge(user);
        }
    }
}
