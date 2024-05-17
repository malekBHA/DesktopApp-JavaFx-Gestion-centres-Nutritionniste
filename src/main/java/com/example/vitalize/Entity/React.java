package com.example.vitalize.Entity;

public class React {
    private int id;
    private Publication publication;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    private Users user;
    private int likeCount;
    private int dislikeCount;

    public React() {
    }

    public React(Publication publication, Users user, int likeCount, int dislikeCount) {
        this.publication = publication;
        this.user = user;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public React(int id, Publication publication, Users user, int likeCount, int dislikeCount) {
        this.id = id;
        this.publication = publication;
        this.user = user;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
