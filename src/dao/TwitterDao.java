package dao;

import entity.*;
import enums.Role;
import enums.Status;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TwitterDao {

    private List<Profile> profileList= new ArrayList<>();
    private List<Post> postList = new LinkedList<>();
    private List<View> viewList = new ArrayList<>();
    private List<Like> likeList = new LinkedList<>();
    private List<Comment> commentList = new ArrayList<>();



    {
        profileList.add(new Profile(1, "Zamir Najatov", "dam_irovich_", "cat", Role.ADMIN, Status.ACTIVE));
        profileList.add(new Profile(10, "Valijon", "vali", "dog", Role.USER, Status.ACTIVE));
        profileList.add(new Profile(11, "Alibek", "ali", "fuck", Role.USER, Status.ACTIVE));
    }



    public Profile getProfileByUsername(String username){
        for( Profile p: profileList){
            if(p.getUserName().equals(username)){
                return p;
            }
        }
        return null;
    }

    public Profile getProfileById(int id){
            for( Profile p: profileList){
                if(p.getId()==(id)){
                    return p;
                }
            }
            return null;
        }


    public void saveProfile(Profile profile){
        profileList.add(profile);
    }

    public void savePost(Post post){
        postList.add(post);
    }

    public void saveView(View view){
        viewList.add(view);
    }

    public void saveLike(Like like) {
        likeList.add(like);
    }

    public void saveComment(Comment comment) {
        commentList.add(comment);
    }


    public List<Profile> getAllUsers(){
        return profileList;
    }

    public List<Post> getAllPosts() {
        return postList;
    }

    public List<View> getViewList() {
        return viewList;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }
}
