package controller;

import entity.Comment;
import entity.Post;
import entity.Profile;
import entity.View;
import enums.Role;
import enums.Status;
import service.TwitterService;
import utils.Util;


import java.util.List;

import static utils.Util.*;

public class TwitterController {

    private static final TwitterService twitterService = new TwitterService();

    public static void start(){
        while(true){
            System.out.print("""
                1.Login
                2.Registration
                0.Exit
                """);
            int menu = inputInt("Choose one");
            switch(menu){
                case 1 -> login();
                case 2 -> registration();
                case 0 -> {return;}
            }
        }
    }

    private static void login() {
        String username = inputStr("Enter username");
        String password = inputStr("Enter your password");
        Role role = twitterService.login(username, password);
        if(role == null) System.out.println("Wrong password or phone");
        else if (role.equals(Role.ADMIN)) adminMenu();
        else if (role.equals(Role.USER)) userMenu();
    }


    private static void registration() {
        String fullName = inputStr("Enter your full name");
        String userName = inputStr("Enter your username");
        String password = inputStr("Enter your password");

        boolean res = twitterService.registration(fullName, userName, password);
        if(res) System.out.println("Succesfully registrated");
        else System.out.println("Failed to register");
    }

// ------------------------ User Menu ----------------------------

    public static void userMenu(){
        while(true){
            System.out.println("""
                    1.Add post
                    2.Show posts
                    3.Delete post
                    4.Show global posts
                    5.Change password
                    0.Log out""");
            int menu = inputInt("Choose one");
            switch (menu){
                case 1 -> addPost();
                case 2 -> showPosts();
                case 3 -> deletePost();
                case 4 -> showGlobalPosts();
                case 5 -> changePassword();
                case 0 -> {return;}
            }
        }
    }


    private static void addPost() {
        String title = inputStr("Enter post title");
        String content = inputStr("Enter content");
        boolean res = twitterService.createPost(title, content);
        if(res) System.out.println("Succesfully added");
        else System.out.println("Failed add to post");
    }

    public static void showPosts(){
        List<Post> posts = twitterService.getPostsByUserId(currentUserId);
        for(Post post : posts){
            System.out.println(post);
        }
        int id = inputInt("Enter post id");
        Post post = twitterService.getPostById(id);
        Profile profile = twitterService.getUserById( post.getUserId() );
        System.out.println("-------" + profile.getUserName() + "-------");
        System.out.println("Title: " + post.getTitle());
        System.out.println("Content: " + post.getContent());
        System.out.println(" View: 100   Comment: 13  Like: 10");
    }

    public static void showGlobalPosts(){
        List<Post> posts = twitterService.getAllPosts();
        postInfo(posts);
    }

    public static void deletePost() {
        List<Post> postsByUserId = twitterService.getPostsByUserId(currentUserId);
        for (Post post : postsByUserId) {
            System.out.println(post);
        }
        int id = inputInt("Choose one");
        boolean res = twitterService.deletePostById(id);
        if(res) System.out.println("Successfully deleted post");
        else System.out.println("Failed to delete post");
    }

    private static void changePassword() {
        String oldPas = inputStr("Enter old password");
        String newPas = inputStr("Create new password");
        boolean res = twitterService.changePassword(currentUserId, oldPas, newPas);
        if(res) System.out.println("Successfully changed");
        else System.out.println("Wrong old password");
    }



    public static void postInfo(List<Post> posts){
        for(Post post : posts){
            System.out.println(post);
        }
        int id = inputInt("Enter post id");
        Post post = twitterService.getPostById(id);
        if(post== null){
            System.out.println("Post not found");
            return;
        }
        Profile profile = twitterService.getUserById( post.getUserId() );

        int viewCount = twitterService.getViewCountByPostId( post.getId() );
        int likeCount = twitterService.getLikeCountByPostId( post.getId() );
        int comments = twitterService.getCommentCountByPostId(post.getId() );


        System.out.println("-------" + profile.getUserName() + "-------");
        System.out.println("Title: " + post.getTitle());
        System.out.println("Content: " + post.getContent());
        System.out.printf(" View: %d   Like: %d  Comment: %d \n", viewCount, likeCount, comments );

        System.out.println("""
                1. Like
                2. Comment""");
        int postMenu = inputInt("Choose one");

        if( postMenu == 1){ twitterService.createLike( currentUserId, post.getId()); }
        else if (postMenu == 2) {
            writeComment( currentUserId, post.getId());
        }

        twitterService.createView(currentUserId, post.getId());
    }

    private static void writeComment(int currentUserId, int postId) {

        List<Comment> list = twitterService.getCommentsByPostId(postId);
        System.out.println("-----Comments-----");
        for (Comment comment : list) {
            System.out.println(comment);
        }

        String comment = inputStr("Write comment");

        twitterService.createComment(userId, postId, comment);

    }


// ------------------------- Admin Menu --------------------------

    public static void adminMenu(){
        while(true){
            System.out.println("""
                    1.Show all users
                    2.Block user
                    3.Active user
                    4.Delete user
                    5.Change password
                    0.Log out""");
            int menu = inputInt("Choose one");
            switch (menu){
                case 1 -> showAllUsers();
                case 2 -> changeUserStatus(Status.BLOCK);
                case 3 -> changeUserStatus(Status.ACTIVE);
                case 4 -> deleteUser();
                case 5 -> changePassword();
                case 0 -> {return;}
            }
        }
    }

    private static void showAllUsers() {
        List<Profile> profiles = twitterService.getAllUsers();
        for (Profile profile : profiles) {
            System.out.println(profile);
        }
    }

    private static void changeUserStatus(Status status) {
        showAllUsers();
        int id = inputInt("Choose one to block");
        twitterService.changeUserStatusById(id, status);
        System.out.println("Status changed!");
    }

    public static void deleteUser(){
        showAllUsers();
        int id = inputInt("Choose one");
        boolean res = twitterService.deleteUserById(id);
        if(res) System.out.println("User successfully deleted");
        else System.out.println("Failed to delete");
    }







}
