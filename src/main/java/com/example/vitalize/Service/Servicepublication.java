package com.example.vitalize.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.vitalize.Entity.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.vitalize.Util.MyDataBase;
import com.example.vitalize.Entity.Commentaire;
import com.example.vitalize.Entity.Publication;
import com.example.vitalize.Entity.React;

public class Servicepublication {

    public Publication getPublicationById(int id) {
        Publication publication = null;
        try {
            String req = "SELECT * FROM publication WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                publication = new Publication();
                publication.setId(rs.getInt(1));
                publication.setIduser(rs.getInt(2));
                publication.setType(rs.getString(3));
                publication.setTitre(rs.getString(4));
                publication.setDescription(rs.getString(5));
                publication.setImage(rs.getString(6));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return publication;
    }

    Connection cnx = MyDataBase.getInstance().getConnection();

    public List<Commentaire> getCommentsForPublication(int publicationId) {
        List<Commentaire> comments = new ArrayList<>();

        try {
            String query = "SELECT * FROM commentaire WHERE id_pub_id = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, publicationId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("id_user_id");
                String contenu = rs.getString("contenu");
                // You may need additional fields from the database

                // Create Commentaire object and add it to the list
                Commentaire commentaire = new Commentaire(id, userId, publicationId, contenu);
                comments.add(commentaire);
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle exceptions as per your application's requirements
        }

        return comments;
    }

        public void add(Publication p) {
            try {

                String req = "INSERT INTO `publication`(`id_user_id`, `type`, `titre`, `description`, `image`) VALUES (?,?,?,?,?)";
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, p.getIduser());
                ps.setString(   2, p.getType());
                ps.setString(3, p.getTitre());
                ps.setString(   4, p.getDescription());
                ps.setString(5, p.getImage());





                ps.executeUpdate();
                System.out.println("Publication Added Successfully!");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }


        }

    public List<React> getAllReactsForPublication(int publicationId) {
        List<React> reacts = new ArrayList<>();
        try {

            Connection cnx = MyDataBase.getInstance().getConnection();
            String query = "SELECT * FROM react WHERE id_pub_id = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setInt(1, publicationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int likeCount = rs.getInt("like_count");
                int dislikeCount = rs.getInt("dislike_count");
                int userId = rs.getInt("id_user_id");


                Servicepublication publicationService = new Servicepublication();
                Publication publication = publicationService.getPublicationById(publicationId);


                UserService userService = new UserService();
                Users user = userService.getUserById(cnx, userId);

                React react = new React(id, publication, user, likeCount, dislikeCount);
                reacts.add(react);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reacts;
    }



    public ObservableList<Publication> fetch() {
            ObservableList<Publication> Publication = FXCollections.observableArrayList();
            try {

                String req = "SELECT * FROM publication";
                Statement st = cnx.createStatement();
                ResultSet rs = st.executeQuery(req);
                while (rs.next()) {
                    Publication p = new Publication();
                    p.setId(rs.getInt(1));
                    p.setIduser(rs.getInt(2));
                    p.setType(rs.getString(3));
                    p.setTitre(rs.getString(4));
                    p.setDescription(rs.getString(5));
                    p.setImage(rs.getString(6));


                    Publication.add(p);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return Publication;
        }


    public List<Publication> getAllPublications() {
        return fetch();
    }


    public void delete(int p) {
            try {
                String req ="DELETE FROM `publication`  WHERE id = ?";
                PreparedStatement ps = cnx.prepareStatement(req);
                ps.setInt(1, p);
                ps.executeUpdate();
                System.out.println("Publication Deleted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        public List<Publication> recherchePublication(int id) {
            List<Publication> Publication = new ArrayList<>();
            try {

                String req = "SELECT * FROM Publication WHERE id LIKE CONCAT(?, '%')";
                PreparedStatement st = cnx.prepareStatement(req);
                st.setInt(1, id);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Publication p = new Publication();
                    p.setId(rs.getInt(1));
                    p.setIduser(rs.getInt(2));
                    p.setType(rs.getString(3));
                    p.setTitre(rs.getString(4));
                    p.setDescription(rs.getString(5));
                    p.setImage(rs.getString(6));
                    Publication.add(p);
                }


            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return Publication;
        }


        public void Edit(int e,String type, String titre, String description, String image) {
            try {
                String req = "UPDATE `Publication` SET `type`=?, `titre`=?, `description`=?, `image`=? WHERE `id`=?";
                PreparedStatement ps = cnx.prepareStatement(req);


                ps.setString(1, type);
                ps.setString(2, titre);
                ps.setString(3, description);
                ps.setString(4, image);

                ps.setInt(5, e);

                // Execute the update
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Publication updated successfully!");
                } else {
                    System.out.println("Failed to update Publication. No matching record found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

    public List<Publication> getRecommendedPublications(String type) {


        // Sample SQL query
        String query = "SELECT * FROM publication WHERE type = ?";

        try {
            // Get database connection
            Connection connection = MyDataBase.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();

            List<Publication> recommendedPublications = new ArrayList<>();
            while (resultSet.next()) {
                Publication publication = new Publication(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_user_id"),
                        resultSet.getString("type"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getString("image")
                );
                recommendedPublications.add(publication);
            }
            return recommendedPublications;
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle exceptions appropriately
            return Collections.emptyList(); // Return empty list in case of error
        }}


}
