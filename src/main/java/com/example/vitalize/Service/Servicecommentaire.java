package com.example.vitalize.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.vitalize.Util.MyDataBase;
import com.example.vitalize.Entity.Commentaire;

public class Servicecommentaire {

    Connection cnx = MyDataBase.getInstance().getConnection();

    public void add(Commentaire c) {
        try {
            String req = "INSERT INTO `commentaire`(`id_user_id`, `id_pub_id`, `contenu`) VALUES (?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getId_user());
            ps.setInt(2, c.getId_pub());
            ps.setString(3, c.getContenu());
            ps.executeUpdate();
            System.out.println("Commentaire Added Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Commentaire getById(int id) {
        Commentaire commentaire = null;
        try {
            String req = "SELECT * FROM commentaire WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setId_user(rs.getInt("id_user_id"));
                commentaire.setId_pub(rs.getInt("id_pub_id"));
                commentaire.setContenu(rs.getString("contenu"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return commentaire;
    }

    public List<Commentaire> fetch() {
        List<Commentaire> commentaires = new ArrayList<>();
        try {
            String req = "SELECT * FROM commentaire";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Commentaire c = new Commentaire();
                c.setId(rs.getInt("id"));
                c.setId_user(rs.getInt("id_user_id"));
                c.setId_pub(rs.getInt("id_pub_id"));
                c.setContenu(rs.getString("contenu"));
                commentaires.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return commentaires;
    }

    public void delete(int id) {
        try {
            String req = "DELETE FROM `commentaire` WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Commentaire Deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Commentaire> rechercheCommentaire(int id) {
        List<Commentaire> commentaires = new ArrayList<>();
        try {
            String req = "SELECT * FROM commentaire WHERE id LIKE CONCAT(?, '%')";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Commentaire c = new Commentaire();
                c.setId(rs.getInt("id"));
                c.setId_user(rs.getInt("id_user_id"));
                c.setId_pub(rs.getInt("id_pub_id"));
                c.setContenu(rs.getString("contenu"));
                commentaires.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return commentaires;
    }

    public void edit(Commentaire c) {
        try {
            String req = "UPDATE `commentaire` SET `id_user_id`=?, `id_pub_id`=?, `contenu`=? WHERE `id`=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, c.getId_user());
            ps.setInt(2, c.getId_pub());
            ps.setString(3, c.getContenu());
            ps.setInt(4, c.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Commentaire updated successfully!");
            } else {
                System.out.println("Failed to update Commentaire. No matching record found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
