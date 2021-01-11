package gr.uom.newsmn;

import com.facebook.AccessToken;

public class FbPost {

    String post;

    public FbPost(String post) {
        this.post = post;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "FbPost{" +
                "post='" + post + '\'' +
                '}';
    }

}
