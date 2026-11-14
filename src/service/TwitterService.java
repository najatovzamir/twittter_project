package service;

import dao.TwitterDao;
import entity.*;
import enums.Role;
import enums.Status;
import utils.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TwitterService {

    private final TwitterDao twitterDao = new TwitterDao();

    public boolean registration(String fullName, String username, String password){
        Profile test = twitterDao.getProfileByUsername(username);
        if( test != null) return false;
        List<Profile> list = twitterDao.getAllUsers();
        for (Profile profile : list) {
            if(profile.getUserName().equals(username)){
                return false;
            }
        }
        Profile profile = new Profile(Util.userId++, fullName, username, password, Role.USER, Status.ACTIVE );
        twitterDao.saveProfile(profile);
        return true;
    }

    public Role login( String username, String password){
        Profile profile = twitterDao.getProfileByUsername(username);
        if( profile == null) return null;
        if(!profile.getPassword().equals(password)) return null;
        Util.currentUserId = profile.getId();
        return profile.getRole();
    }


    public boolean createPost(String title, String content) {
        if( title == null) return false;
        if(title.isBlank()) return false;
        Profile profile = twitterDao.getProfileById(Util.currentUserId);
        if(profile.getStatus().equals(Status.BLOCK)) return false;
        Post post = new Post(Util.postId++, title, content, Util.currentUserId);
        twitterDao.savePost(post);
        return true;
    }


    public List<Post> getPostsByUserId(int userId) {
        List<Post> list = twitterDao.getAllPosts();
        List<Post> reslist = new ArrayList<>();
        for (Post post : list) {
            if(post.getUserId() == userId){
                reslist.add(post);
            }
        }
        return reslist;
    }


    public List<Post> getAllPosts(){
        return twitterDao.getAllPosts();
    }

    public boolean deletePostById(int id) {
        List<Post> posts = getAllPosts();
        for (Post post : posts) {
            if(post.getId() == id && post.getUserId() == Util.currentUserId){
                posts.remove(post);
                return true;
            }
        }
        return false;
    }

// ------------------------ Admin Menu ------------------------------------

    public List<Profile> getAllUsers() {
        return twitterDao.getAllUsers();
    }

    public void changeUserStatusById(int id, Status status) {
        List<Profile> profiles = twitterDao.getAllUsers();
        for (Profile profile : profiles) {
            if(profile.getId() == id){
                profile.setStatus(status);
                break;
            }
        }
    }

    public void activateUserById(int id) {
        List<Profile> profiles = twitterDao.getAllUsers();
        for (Profile profile : profiles) {
            if(profile.getId() == id){
                profile.setStatus(Status.ACTIVE);
                break;
            }
        }
    }

    public Post getPostById(int id) {
        List<Post> posts = twitterDao.getAllPosts();
        for (Post post : posts) {
            if(post.getId() == id) return post;
        }
        return null;
    }

    public Profile getUserById(int id) {
        return twitterDao.getProfileById(id);
    }


    public boolean changePassword(int currentUserId, String oldPas, String newPas) {
        Profile profile = twitterDao.getProfileById(currentUserId);
        if( profile.getPassword().equals(oldPas)) {
            profile.setPassword(newPas);
            return true;
        }
        return false;
    }


    public boolean deleteUserById(int id) {
        List<Profile> profiles = twitterDao.getAllUsers();
        for (Profile profile : profiles) {
            if(profile.getId() == id){
                profiles.remove(profile);
                return true;
            }
        }
        return false;
    }

    public void createView(int userId, int postId) {
        View view = new View(userId, postId);
        twitterDao.saveView(view);
    }

    public int getViewCountByPostId(int postId) {
        List<View> viewList = twitterDao.getViewList();
        int count = 0;
        for (View view: viewList){
            if( view.getPostId() == postId){
                count++;
            }
        }
        return count;
    }


    public void createLike(int userId, int postId) {
        List<Like> likeList = twitterDao.getLikeList();
        Iterator<Like> iterator = likeList.iterator();

        // Iterator bilan
        while(iterator.hasNext()){
            Like like = iterator.next();
            if(like.getPostId() == postId && like.getUserId() == userId){
                iterator.remove();
                return;
            }
        }
        //For loop bilan
        /*for (Like like : likeList) {
            if( like.getUserId() == userId && like.getPostId() == postId){
                likeList.remove(like);
                return;
            }
        }*/
        Like like = new Like(userId, postId);
        twitterDao.saveLike(like);
    }

    public int getLikeCountByPostId(int postId) {
        List<Like> likeList = twitterDao.getLikeList();
        int count = 0;
        for(Like like: likeList){
            if( like.getPostId() == postId){
                count++;
            }
        }
        return count;
    }

    public void createComment(int userId, int postId, String comment) {
        Comment commentObj = new Comment(userId, postId, comment);
        twitterDao.saveComment(commentObj);
    }

    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> commentList = twitterDao.getCommentList();
        List<Comment> resList = new ArrayList<>();
        for (Comment comment : commentList) {
            if( comment.getPostId() == postId){
                commentList.add(comment);
            }
        }
        return resList;
    }

    public int getCommentCountByPostId(int postId) {
        List<Comment> commentsByPostId = getCommentsByPostId(postId);
        return commentsByPostId.size();
    }
}
