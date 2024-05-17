package com.example.vitalize.Service;

import interfaces.IService;
import com.example.vitalize.Entity.FichePatient;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Util.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FichePatientService implements IService<FichePatient> {
    Connection cnx = MyDataBase.getInstance().getConnection();

    public void add(FichePatient fichePatient) {
        String req = "INSERT INTO `fichepatient` (`weight`, `muscle_mass`, `height`, `allergies`, `illnesses`, `breakfast`, `midday`, `dinner`, `snacks`, `calories`, `other`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            setFichePatientParameters(preparedStatement, fichePatient);
            preparedStatement.executeUpdate();
            System.out.println("FichePatient added successfully.");
        } catch (SQLException e) {
            System.err.println("Error occurred while adding FichePatient: " + e.getMessage());
        }
    }

    public void update(FichePatient fichePatient) {
        String req = "UPDATE fichepatient SET weight = ?, muscle_mass = ?, height = ?, allergies = ?, illnesses = ?, breakfast = ?, midday = ?, dinner = ?, snacks = ?, calories = ?, other = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            setFichePatientParameters(preparedStatement, fichePatient);
            preparedStatement.setInt(12, fichePatient.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("FichePatient with ID " + fichePatient.getId() + " updated successfully.");
            } else {
                System.out.println("No FichePatient found with ID " + fichePatient.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while updating FichePatient with ID " + fichePatient.getId() + ": " + e.getMessage());
        }
    }

    public void delete(FichePatient fichePatient) {
        String req = "DELETE FROM fichepatient WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, fichePatient.getId());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("FichePatient with ID " + fichePatient.getId() + " deleted successfully.");
            } else {
                System.out.println("No FichePatient found with ID " + fichePatient.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while deleting FichePatient with ID " + fichePatient.getId() + ": " + e.getMessage());
        }
    }

    public List<FichePatient> getAll() {
        List<FichePatient> fichePatients = new ArrayList<>();
        String req = "SELECT * FROM fichepatient";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                fichePatients.add(mapResultSetToFichePatient(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching all FichePatients: " + e.getMessage());
        }
        return fichePatients;
    }

    public FichePatient getOne(int id) {
        FichePatient fichePatient = null;
        String req = "SELECT * FROM fichepatient WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    fichePatient = mapResultSetToFichePatient(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching FichePatient with ID " + id + ": " + e.getMessage());
        }
        return fichePatient;
    }

    public List<Users> getAllUserNames() {
        List<Users> userList = new ArrayList<>();
        String req = "SELECT nom, email FROM users";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Users user = new Users();
                user.setNom(resultSet.getString("nom"));
                user.setEmail(resultSet.getString("email"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching user names: " + e.getMessage());
        }
        return userList;
    }


    public int getUserId(String userName) {
        int userId = -1;
        String req = "SELECT id FROM users WHERE nom = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            preparedStatement.setString(1, userName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while fetching user ID: " + e.getMessage());
        }
        return userId;
    }

    private void setFichePatientParameters(PreparedStatement preparedStatement, FichePatient fichePatient) throws SQLException {
        preparedStatement.setInt(1, fichePatient.getWeight());
        preparedStatement.setInt(2, fichePatient.getMuscleMass());
        preparedStatement.setInt(3, fichePatient.getHeight());
        preparedStatement.setString(4, fichePatient.getAllergies());
        preparedStatement.setString(5, fichePatient.getIllnesses());
        preparedStatement.setString(6, fichePatient.getBreakfast());
        preparedStatement.setString(7, fichePatient.getMidday());
        preparedStatement.setString(8, fichePatient.getDinner());
        preparedStatement.setString(9, fichePatient.getSnacks());
        preparedStatement.setInt(10, fichePatient.getCalories());
        preparedStatement.setString(11, fichePatient.getOther());
    }

    private FichePatient mapResultSetToFichePatient(ResultSet resultSet) throws SQLException {
        FichePatient fichePatient = new FichePatient();
        fichePatient.setId(resultSet.getInt("id"));
        fichePatient.setWeight(resultSet.getInt("weight"));
        fichePatient.setMuscleMass(resultSet.getInt("muscle_mass"));
        fichePatient.setHeight(resultSet.getInt("height"));
        fichePatient.setAllergies(resultSet.getString("allergies"));
        fichePatient.setIllnesses(resultSet.getString("illnesses"));
        fichePatient.setBreakfast(resultSet.getString("breakfast"));
        fichePatient.setMidday(resultSet.getString("midday"));
        fichePatient.setDinner(resultSet.getString("dinner"));
        fichePatient.setSnacks(resultSet.getString("snacks"));
        fichePatient.setCalories(resultSet.getInt("calories"));
        fichePatient.setOther(resultSet.getString("other"));
        return fichePatient;
    }

    public List<FichePatient> Afficher() {
        return getAll();
    }
}
