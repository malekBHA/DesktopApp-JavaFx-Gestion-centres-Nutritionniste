package com.example.vitalize.Service;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Entity.participation;
import com.example.vitalize.Util.MyDataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    Connection cnx = MyDataBase.getInstance().getConnection();



    
    public void add(evenement p) {
        try {

            String req = "INSERT INTO `evenement`(`nom`, `date`, `localisation`, `capacite`, `organisateur`, `description`, `image_eve`) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, p.getNom());
            ps.setDate(   2, p.getDate());
            ps.setString(3, p.getLocalisation());
            ps.setInt(   4, p.getCapacite());
            ps.setString(5, p.getOrangisateur());
            ps.setString(6, p.getDescription());
            ps.setString(7, p.getImage());



            ps.executeUpdate();
            System.out.println("evenement Added Successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }


    }




    
    public ObservableList<evenement> fetch() {
        ObservableList<evenement> evenements = FXCollections.observableArrayList();
        try {

            String req = "SELECT * FROM evenement";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                evenement p = new evenement();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setDate(rs.getDate(3));
                p.setLocalisation(rs.getString(4));
                p.setCapacite(rs.getInt(5));
                p.setOrangisateur(rs.getString(6));
                p.setDescription(rs.getString(7));
                p.setImage(rs.getString(8));
                String s="";
                Activityservice as=new Activityservice();
                p.setActivites(as.fetch().stream().filter(c->c.getIdevent()==p.getId()).toList());
                s="";
                for(activite a :p.getActivites()){
                    s+=a.getType_activite()+"\n \t       ";
                }
                p.setS(s);
                evenements.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return evenements;
    }

    
    public  List<participation> searchpart(){
        ObservableList<participation> participations = FXCollections.observableArrayList();
        try {

            String req = "SELECT * FROM reservation";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                participation p = new participation();
                p.setIdevent(rs.getInt(3));
                p.setIduser(rs.getInt(2));
                participations.add(p);
            }}
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return participations;
    }
    public void addp(participation p) {
        try {
            String req = "INSERT INTO `reservation`(`id_user`, `id_event`) VALUES (?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getIduser());
            ps.setInt(   2, p.getIdevent());
            ps.executeUpdate();
            System.out.println("Participation Added Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void delete(int p) {
        try {
            String req ="DELETE FROM `evenement`  WHERE idevenement = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p);
            ps.executeUpdate();
            System.out.println("Participation Deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void deletep(int p) {
        try {
            String req ="DELETE FROM `reservation`  WHERE id_event = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p);
            ps.executeUpdate();
            System.out.println("evenement Deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public List<evenement> rechercheevenement(int id) {
        List<evenement> evenements = new ArrayList<>();
        try {

            String req = "SELECT * FROM evenement WHERE idevenement LIKE CONCAT(?, '%')";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                evenement p = new evenement();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setDate(rs.getDate(3));
                p.setLocalisation(rs.getString(4));
                p.setCapacite(rs.getInt(5));
                p.setOrangisateur(rs.getString(6));
                p.setDescription(rs.getString(7));
                p.setImage(rs.getString(8));

                evenements.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return evenements;
    }

    public void Edit(int e,String nomevenement, Date date, String localisation, int capacite, String organisateur, String description, String image) {
        try {
            String req = "UPDATE `evenement` SET `nom`=?, `date`=?, `localisation`=?, `capacite`=?, `organisateur`=?, `description`=?, `image_eve`=? WHERE `idevenement`=?";
            PreparedStatement ps = cnx.prepareStatement(req);

            // Set values for each parameter
            ps.setString(1, nomevenement);
            ps.setDate(2, date);
            ps.setString(3, localisation);
            ps.setInt(4, capacite);
            ps.setString(5, organisateur);
            ps.setString(6, description);
            ps.setString(7, image);

            ps.setInt(8, e);

            // Execute the update
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("evenement updated successfully!");
            } else {
                System.out.println("Failed to update evenement. No matching record found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
