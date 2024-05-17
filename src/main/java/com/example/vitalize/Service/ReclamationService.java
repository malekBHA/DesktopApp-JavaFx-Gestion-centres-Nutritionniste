package com.example.vitalize.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Util.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReclamationService implements IService<Reclamation> {
    Connection cnx = MyDataBase.getInstance().getConnection();

    @Override
    public void add(Reclamation reclamation) {
        String req = "INSERT INTO Reclamation (sujet, etat, type, description, file, date, user_id, medecin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getEtat());
            ps.setString(3, reclamation.getType());
            ps.setString(4, reclamation.getDescription());
            ps.setString(5, reclamation.getFile());
            ps.setDate(6, new java.sql.Date(reclamation.getDate().getTime()));
            ps.setInt(7, reclamation.getUser_id());
            ps.setInt(8, reclamation.getMedecin());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding reclamation failed, no rows affected.");
            }
            System.out.println("Reclamation added successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add reclamation", e);
        }
    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String req = "SELECT * FROM Reclamation";
        try (Statement st = cnx.createStatement();
             ResultSet res = st.executeQuery(req)) {
            while (res.next()) {
                int id = res.getInt("id");
                String sujet = res.getString("sujet");
                String etat = res.getString("etat");
                String type = res.getString("type");
                String description = res.getString("description");
                String file = res.getString("file");
                Date date = res.getDate("date");
                int user_id = res.getInt("user_id");
                int medecin = res.getInt("medecin");

                Reclamation reclamation = new Reclamation(sujet, etat, type, description, file, date, id, user_id, medecin);
                reclamations.add(reclamation);
            }
            System.out.println("Reclamations retrieved successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve reclamations", e);
        }
        return reclamations;
    }





@Override
public void delete(Reclamation reclamation)  {
        String req = "DELETE FROM Reclamation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, reclamation.getId()); // Set the id parameter for the WHERE clause
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting reclamation failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void update(Reclamation reclamation) {
        String req = "UPDATE Reclamation SET sujet=?, description=?, medecin=?, file=?, type=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, reclamation.getSujet());
            ps.setString(2, reclamation.getDescription());
            ps.setInt(3, reclamation.getMedecin());
            ps.setString(4, reclamation.getFile());
            ps.setString(5, reclamation.getType());
            ps.setInt(6, reclamation.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating reclamation failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reclamation getOne(int id) {
        String req = "SELECT * FROM Reclamation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                // Retrieve attributes from the database
                String sujet = res.getString("sujet");
                String etat = res.getString("etat");
                String type = res.getString("type");
                String description = res.getString("description");
                String file = res.getString("file");
                Date date = res.getDate("date");
                int user_id = res.getInt("user_id");
                int medecin = res.getInt("medecin");

                // Create and return a new Reclamation object using the constructor
                return new Reclamation(sujet, etat, type, description, file, date, id, user_id, medecin);
            } else {
                throw new SQLException("Reclamation with id " + id + " not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEtat(int reclamationId, String newEtat) {
        String req = "UPDATE Reclamation SET etat = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, newEtat);
            ps.setInt(2, reclamationId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating reclamation etat failed, no rows affected.");
            }
            System.out.println("Reclamation etat updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update reclamation etat", e);
        }
    }

    public int countUnrepliedReclamations(long userId) {
        try {
            var connection = MyDataBase.getInstance().getConnection();
            // Query to count reclamations that have no responses
            String query = "SELECT COUNT(*) FROM reclamation WHERE user_id = ? AND id NOT IN (SELECT reclamation_id FROM reponse WHERE reclamation_id IS NOT NULL)";
            var preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setLong(1, userId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }





}
