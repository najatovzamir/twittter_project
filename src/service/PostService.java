package service;

import entity.Post;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostService {

    private static final String FILE_PATH = "posts/post.txt";

    public static void main(String[] args) {


        /*File file = new File(FILE_PATH);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/


        /*Post post = new Post(3, "Football", "El-Clasico", 4);
        savePost(post);*/

        /*for (Post post : readPosts()) {
            System.out.println(post);
        }*/

        Post post = new Post(0, "Kitob", "Kitob bizning do'timiz", 10);
        Post post1 = new Post(1, "Qushlar", "Qushlar bizning do'stimiz", 11);
        savePost(post);
        savePost(post1);



        deletePostById(2);

    }


    public static void savePost(Post post){
        BufferedWriter writer = null;
        try {

            writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(post.getId() + "#" + post.getTitle() + "#" + post.getContent() + "#" + post.getUserId());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Post> readPosts(){
        List<Post> postList = new ArrayList<>();
        BufferedReader reader = null;
        try {

            reader = new BufferedReader( new FileReader(FILE_PATH));

            String line = reader.readLine();
            while (line != null){
                String[] split = line.split("#");

                Post post = new Post(Integer.parseInt(split[0]), split[1], split[2], Integer.parseInt(split[3]));
                postList.add(post);

                line = reader.readLine();
            }
            return postList;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyList();

    }

    public static void deletePostById(int id){
        List<Post> posts = readPosts();
        posts.removeIf( (Post post) -> post.getId() == id);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Post post : posts) {
            savePost(post);
        }
    }

}
