package com.example;

import com.example.config.ApplicationConfig;
import com.example.entity.User;
import com.example.entity.Post;
import com.example.entity.Role;
import com.example.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class Application {
    public static void main(String[] args) {
        System.out.println("=== Spring 6 JPA CRUD Demo ===\n");

        // Initialize Spring context
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ApplicationConfig.class);
        UserService userService = context.getBean(UserService.class);
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. User Management");
                System.out.println("2. Post Management");
                System.out.println("3. Role Management");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        userManagementMenu(userService, scanner);
                        break;
                    case 2:
                        postManagementMenu(userService, scanner);
                        break;
                    case 3:
                        roleManagementMenu(userService, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        context.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
            context.close();
        }
    }

    private static void userManagementMenu(UserService userService, Scanner scanner) {
        while (true) {
            System.out.println("\nUser Management:");
            System.out.println("1. Create User");
            System.out.println("2. Read User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    User user = userService.createUser(username, email);
                    System.out.println("Created: " + user);
                    break;
                case 2:
                    System.out.print("Enter user ID: ");
                    Long userId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    Optional<User> foundUser = userService.findUserById(userId);
                    foundUser.ifPresentOrElse(
                            u -> System.out.println("Found: " + u),
                            () -> System.out.println("User not found")
                    );
                    break;
                case 3:
                    System.out.print("Enter user ID: ");
                    Long updateUserId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    userService.updateUserEmail(updateUserId, newEmail);
                    System.out.println("User updated");
                    break;
                case 4:
                    System.out.print("Enter user ID: ");
                    Long deleteUserId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    userService.deleteUser(deleteUserId);
                    System.out.println("User deleted");
                    break;
                case 5:
                    List<User> allUsers = userService.findAllUsers();
                    System.out.println("All Users:");
                    allUsers.forEach(u -> System.out.println("  " + u));
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void postManagementMenu(UserService userService, Scanner scanner) {
        while (true) {
            System.out.println("\nPost Management:");
            System.out.println("1. Create Post");
            System.out.println("2. Read Post");
            System.out.println("3. Update Post");
            System.out.println("4. Delete Post");
            System.out.println("5. List All Posts");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter user ID: ");
                    long userId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter post title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter post content: ");
                    String content = scanner.nextLine();
                    Post post = userService.createPost(userId, title, content);
                    System.out.println("Created: " + post);
                    break;
                case 2:
                    System.out.print("Enter post ID: ");
                    Long postId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline

//                    Optional<Post> foundPosts = userService.findPostById(postId);
//                    foundPosts.ifPresentOrElse(
//                            u -> System.out.println("Found: " + u),
//                            () -> System.out.println("Post not found")
//                    );

                    List<Post> foundPosts = userService.findPostsByUser(postId);
                    if (foundPosts.isEmpty()) {
                        System.out.println("No posts found ");
                    } else {
                        System.out.println("Posts");
                        for (Post p : foundPosts) {
                            System.out.println("Post ID: " + p.getId());
                            System.out.println("Title: " + p.getTitle());
                            System.out.println("Content: " + p.getContent());
                            System.out.println("Created At: " + p.getCreatedAt());
                            System.out.println("Author ID: " + p.getAuthor().getId());
                            System.out.println("-----------------------------");
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter post ID: ");
                    Long updatePostId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new content: ");
                    String newContent = scanner.nextLine();
                    userService.updatePost(updatePostId, newTitle, newContent);
                    System.out.println("Post updated");
                    break;
                case 4:
                    System.out.print("Enter post ID: ");
                    Long deletePostId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    userService.deletePost(deletePostId);
                    System.out.println("Post deleted");
                    break;
                case 5:
                    List<Post> allPosts = userService.findAllPosts();
                    System.out.println("All Posts:");
                    allPosts.forEach(p -> System.out.println("  " + p));
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void roleManagementMenu(UserService userService, Scanner scanner) {
        while (true) {
            System.out.println("\nRole Management:");
            System.out.println("1. Create Role");
            System.out.println("2. Read Role");
            System.out.println("3. Update Role");
            System.out.println("4. Delete Role");
            System.out.println("5. List All Roles");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter role name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter role description: ");
                    String description = scanner.nextLine();
                    Role role = userService.createRole(name, description);
                    System.out.println("Created: " + role);
                    break;
                case 2:
                    System.out.print("Enter role ID: ");
                    Long roleId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    Optional<Role> foundRole = userService.findRoleById(roleId);
                    foundRole.ifPresentOrElse(
                            r -> System.out.println("Found: " + r),
                            () -> System.out.println("Role not found")
                    );
                    break;
                case 3:
                    System.out.print("Enter role ID: ");
                    Long updateRoleId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new role name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new role description: ");
                    String newDescription = scanner.nextLine();
                    userService.updateRole(updateRoleId, newName, newDescription);
                    System.out.println("Role updated");
                    break;
                case 4:
                    System.out.print("Enter role ID: ");
                    Long deleteRoleId = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    userService.deleteRole(deleteRoleId);
                    System.out.println("Role deleted");
                    break;
                case 5:
                    List<Role> allRoles = userService.findAllRoles();
                    System.out.println("All Roles:");
                    allRoles.forEach(r -> System.out.println("  " + r));
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
