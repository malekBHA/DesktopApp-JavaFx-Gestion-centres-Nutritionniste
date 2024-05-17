package com.example.vitalize.Service;

import com.example.vitalize.Entity.Reponse;
import com.example.vitalize.Util.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReponseService implements IService<Reponse> {

    Connection cnx = MyDataBase.getInstance().getConnection();

    @Override
    public void add(Reponse reponse) {
        String req = "INSERT INTO Reponse (message, date, reclamation_id, patient) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            // Set parameters
            ps.setString(1, reponse.getMessage());
            ps.setDate(2, new java.sql.Date(reponse.getDate().getTime()));
            ps.setInt(3, reponse.getReclamation_id());
            ps.setInt(4, reponse.getPatient());

            ps.executeUpdate();

            System.out.println("Reponse added successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding reponse", e);
        }
    }

    @Override
    public void update(Reponse reponse) {
        String req = "UPDATE Reponse SET message = ?, date = ?, reclamation_id = ?, patient = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            // Set parameters
            ps.setString(1, reponse.getMessage());
            ps.setDate(2, new java.sql.Date(reponse.getDate().getTime()));
            ps.setInt(3, reponse.getReclamation_id());
            ps.setInt(4, reponse.getPatient());
            ps.setInt(5, reponse.getId());

            // Execute the update
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("No reponse found with ID " + reponse.getId());
            }

            System.out.println("Reponse updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating reponse", e);
        }
    }

    @Override
    public void delete(Reponse reponse) {
        String req = "DELETE FROM Reponse WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            // Set the ID parameter
            ps.setInt(1, reponse.getId());

            // Execute the delete
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("No reponse found with ID " + reponse.getId());
            }

            System.out.println("Reponse deleted successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting reponse", e);
        }
    }





    @Override
    public List<Reponse> getAll() {
        List<Reponse> reponses = new ArrayList<>();
        String req = "SELECT * FROM Reponse";
        try (PreparedStatement ps = cnx.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Retrieve attributes from the database
                int id = rs.getInt("id");
                String message = rs.getString("message");
                Date date = rs.getDate("date");
                int reclamationId = rs.getInt("reclamation_id");
                int patient = rs.getInt("patient");

                Reponse reponse = new Reponse(id,reclamationId, patient ,message, date);

                reponses.add(reponse);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving reponses", e);
        }
        return reponses;
    }


    @Override
    public Reponse getOne(int id) {
        String req = "SELECT * FROM Reponse WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Retrieve attributes from the database
                    String message = rs.getString("message");
                    Date date = rs.getDate("date");
                    int reclamationId = rs.getInt("reclamation_id");
                    int patient = rs.getInt("patient");

                    // Create a Reponse object
                    Reponse reponse = new Reponse(id,reclamationId, patient ,message, date);
                    return reponse;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving reponse with ID " + id, e);
        }
        return null;
    }

    public boolean hasRelatedResponses(int reclamationId) {
        String req = "SELECT COUNT(*) AS count FROM Reponse WHERE reclamation_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, reclamationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking for related responses", e);
        }
        return false;
    }



}
