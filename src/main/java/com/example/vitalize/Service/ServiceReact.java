package com.example.vitalize.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.vitalize.Entity.Users;
import com.example.vitalize.Util.MyDataBase;
import com.example.vitalize.Entity.Publication;
import com.example.vitalize.Entity.React;

public class ServiceReact {

    private static ServiceReact instance;
    private Connection cnx;

    public ServiceReact() {
        // Establish database connection
        this.cnx = MyDataBase.getInstance().getConnection();
    }

    public static ServiceReact getInstance() {
        if (instance == null) {
            instance = new ServiceReact();
        }
        return instance;
    }

    public void add(React react) {
        try {
            String req = "INSERT INTO `react`(`id_user_id`, `id_pub_id`, `like_count`, `dislike_count`) VALUES (?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, react.getUser().getId());
            ps.setInt(2, react.getPublication().getId());
            ps.setInt(3, react.getLikeCount());
            ps.setInt(4, react.getDislikeCount());
            ps.executeUpdate();
            System.out.println("React Added Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            String req = "DELETE FROM `react` WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("React Deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void edit(React react) {
        try {
            String req = "UPDATE `react` SET `id_user_id`=?, `id_pub_id`=?, `like_count`=?, `dislike_count`=? WHERE `id`=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, react.getUser().getId());
            ps.setInt(2, react.getPublication().getId());
            ps.setInt(3, react.getLikeCount());
            ps.setInt(4, react.getDislikeCount());
            ps.setInt(5, react.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("React updated successfully!");
            } else {
                System.out.println("Failed to update React. No matching record found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public React getReactByUserAndPublication(Users user, Publication publication) {
        try {
            String req = "SELECT * FROM react WHERE id_pub_id = ? AND id_user_id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, publication.getId());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                int id = rs.getInt("id");
                int likeCount = rs.getInt("like_count");
                int dislikeCount = rs.getInt("dislike_count");
                React react = new React(id, publication, user, likeCount, dislikeCount);
                return react;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null; // Return null if no matching React object is found
    }
}
