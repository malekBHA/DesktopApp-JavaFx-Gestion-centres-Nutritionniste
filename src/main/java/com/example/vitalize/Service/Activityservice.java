package com.example.vitalize.Service;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Util.MyDataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Activityservice {
    Connection cnx = MyDataBase.getInstance().getConnection();



    public void add(activite p) {
        try {

            String req = "INSERT INTO `activite`(`type_activite`, `description`, `duree`, `idEvenement`, `Image_act`) VALUES (?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getType_activite());
            ps.setString(   2, p.getDescription());
            ps.setInt(3, p.getDuree());
            ps.setInt(   4, p.getIdevent());
            ps.setString(5, p.getImage());




            ps.executeUpdate();
            System.out.println("activite Added Successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }




    public ObservableList<activite> fetch() {
        ObservableList<activite> activites = FXCollections.observableArrayList();
        try {

            String req = "SELECT * FROM activite";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                activite p = new activite();
                p.setId(rs.getInt(1));
                p.setType_activite(rs.getString(2));
                p.setDescription(rs.getString(3));
                p.setDuree(rs.getInt(4));
                p.setIdevent(rs.getInt(5));
                p.setImage(rs.getString(6));

                activites.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return activites;
    }



    public void delete(int p) {
        try {
            String req ="DELETE FROM `activite`  WHERE id_activite = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p);
            ps.executeUpdate();
            System.out.println("activite Deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public List<activite> rechercheactivite(int id) {
        List<activite> activites = new ArrayList<>();
        try {

            String req = "SELECT * FROM `activite` WHERE id_activite LIKE CONCAT(?, '%')";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                activite p = new activite();
                p.setId(rs.getInt(1));
                p.setType_activite(rs.getString(2));
                p.setDescription(rs.getString(3));
                p.setDuree(rs.getInt(4));
                p.setIdevent(rs.getInt(5));
                p.setImage(rs.getString(6));

                activites.add(p);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return activites;
    }
/*
    public List<activite> filtreactivite(String Muscle) {
        List<activite> activites = new ArrayList<>();
        try {

            String req = "SELECT * FROM activite WHERE Muscle = ?";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                activite p = new activite();
                p.setIdactivite(rs.getInt(1));
                p.setNomactivite(rs.getString(2));
                p.setIdCoach(rs.getInt(3));
                p.setDifficulteactivite(rs.getString(4));
                p.setEvaluationactivite(rs.getInt(5));
                p.setMuscle(rs.getString(6));
                p.setDemonstration(rs.getString(7));

                activites.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return activites;}*/

    public void Edit(int e,String nomactivite, String desc, int duree ,String image) {
        try {
            String req = "UPDATE `activite` SET `type_activite`=?, `description`=?, `duree`=?,`image_act`=? WHERE `id_activite`=?";
            PreparedStatement ps = cnx.prepareStatement(req);

            // Set values for each parameter
            ps.setString(1, nomactivite);
            ps.setString(2, desc);
            ps.setInt(3, duree);
            ps.setString(4, image);
            ps.setInt(5, e);


            // Execute the update
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("activite updated successfully!");
            } else {
                System.out.println("Failed to update activite. No matching record found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
