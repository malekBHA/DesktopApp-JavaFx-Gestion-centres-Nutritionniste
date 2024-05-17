package services;


import modals.Users;
import util.MaConnexion;
import org.json.JSONArray;


import java.sql.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;


import java.util.List;


public class UserService implements UserServiceInterface<Users>{
    private final Connection cnx;
    public UserService()
    {
        cnx= MaConnexion.getInstance().getConnection();;
    }
    @Override
    public Users login(Users t) throws SQLException {
       Users u  =this.findByEmail(t.getEmail());
       if(u==null )
        return null;
      else {
           if (BCrypt.checkpw(t.getPassword(), u.getPassword())) {
               return u;
           }else{
               u.setId(0);
               return u;
           }
       }
    }

    @Override
    public int registre(Users t) throws SQLException {
        String hashedPassword = BCrypt.hashpw(t.getPassword(), BCrypt.gensalt());
        JSONArray m = new JSONArray();
        int status=0;
        m.put(t.getRole());
        if(t.isStatus())
            status=1;
        String req = "INSERT INTO users (email, roles, password, nom, prenom, status, tel, avatar, num_cnam, adresse) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = cnx.prepareStatement(req);
        preparedStatement.setString(1, t.getEmail());
        preparedStatement.setString(2, m.toString());
        preparedStatement.setString(3, hashedPassword);
        preparedStatement.setString(4, t.getNom());
        preparedStatement.setString(5, t.getPrenom());
        preparedStatement.setInt(6, status);
        preparedStatement.setString(7, t.getTel());
        preparedStatement.setString(8, t.getAvatar());
        preparedStatement.setString(9, t.getNumcnam());
        preparedStatement.setString(10, t.getAdresse());

        int rows = preparedStatement.executeUpdate();

        if (rows > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Users> getAllusers() throws SQLException {
        List<Users> Users = new ArrayList<>();
        String req = "SELECT * " +
                "FROM users ";
        Statement statement = cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(req);
        while (resultSet.next()) {
            Users user = new Users();
           user.setId(resultSet.getInt("id"));
            user.setNom(resultSet.getString("nom"));
            user.setPrenom(resultSet.getString("prenom"));
            user.setRole(resultSet.getString("roles"));
            user.setNumcnam(resultSet.getString("num_cnam"));
            user.setTel(resultSet.getString("tel"));
            int status=resultSet.getInt("status");
            if(status==1) user.setStatus(true); else user.setStatus(false);
            user.setAdresse(resultSet.getString("adresse"));
            user.setAvatar(resultSet.getString("avatar"));
            user.setResetToken(resultSet.getString("reset_token"));
            user.setVerif(resultSet.getString("verif"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            Users.add(user);
        }
        return Users;
    }

    @Override
    public Users findById(int id) throws SQLException {
        Users user=new Users();
        String req = "SELECT * " +
                "FROM users u " +
                "WHERE u.id = " + id;
        Statement statement = cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(req);
        if (resultSet.next()) {
            user.setId(resultSet.getInt("id"));
            user.setNom(resultSet.getString("nom"));
            user.setPrenom(resultSet.getString("prenom"));
            user.setRole(resultSet.getString("roles"));
            user.setNumcnam(resultSet.getString("num_cnam"));
            user.setTel(resultSet.getString("tel"));
            int status=resultSet.getInt("status");
            if(status==1) user.setStatus(true); else user.setStatus(false);
            user.setAdresse(resultSet.getString("adresse"));
            user.setAvatar(resultSet.getString("avatar"));
            user.setResetToken(resultSet.getString("reset_token"));
            user.setVerif(resultSet.getString("verif"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Users findByEmail(String email) throws SQLException {
        Users user=new Users();
        String req = "SELECT * " +
                "FROM users u " +
                "WHERE u.email = '" + email + "'";
        Statement statement = cnx.createStatement();
        ResultSet resultSet = statement.executeQuery(req);
        if (resultSet.next()) {
            user.setId(resultSet.getInt("id"));
            user.setNom(resultSet.getString("nom"));
            user.setPrenom(resultSet.getString("prenom"));
            user.setRole(resultSet.getString("roles"));
            user.setNumcnam(resultSet.getString("num_cnam"));
            user.setTel(resultSet.getString("tel"));
            int status=resultSet.getInt("status");
            if(status==1) user.setStatus(true); else user.setStatus(false);
            user.setAdresse(resultSet.getString("adresse"));
            user.setAvatar(resultSet.getString("avatar"));
            user.setResetToken(resultSet.getString("reset_token"));
            user.setVerif(resultSet.getString("verif"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Users ModifierUser(Users t,String email) throws SQLException {
        Users u=this.findByEmail(email);
        if(u==null)
            return null;
        else {
            String req = "UPDATE users SET " +
                    "nom = ?, " +
                    "prenom = ?, " +
                    "adresse = ?, " +
                    "tel = ?, " +
                    "email = ? " +
                    "WHERE email = ?";

            PreparedStatement preparedStatement = cnx.prepareStatement(req);
            preparedStatement.setString(1, t.getNom());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getAdresse());
            preparedStatement.setString(4, t.getTel());
            preparedStatement.setString(5, t.getEmail());
            preparedStatement.setString(6, email);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return t;
            } else {
                return u;
            }
        }
    }

    public Users ModifierUserAdmin(Users t,String email) throws SQLException {
        Users u=this.findByEmail(email);
        if(u==null)
            return null;
        else {
            JSONArray m = new JSONArray();
            m.put(t.getRole());
            String req = "UPDATE users SET " +
                    "nom = ?, " +
                    "prenom = ?, " +
                    "adresse = ?, " +
                    "tel = ?, " +
                    "roles = ?, " +
                    "email = ? " +
                    "WHERE email = ?";

            PreparedStatement preparedStatement = cnx.prepareStatement(req);
            preparedStatement.setString(1, t.getNom());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getAdresse());
            preparedStatement.setString(4, t.getTel());
            preparedStatement.setString(5, m.toString());
            preparedStatement.setString(6, t.getEmail());
            preparedStatement.setString(7, email);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return t;
            } else {
                return u;
            }
        }
    }

    @Override
    public int supprimerUser(int id) throws SQLException {
        Users u=this.findById(id);
        if(u==null)
        return 0;
        else {
            int rowsDeleted;
            String reqDelete = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement2 = cnx.prepareStatement(reqDelete)) {
                statement2.setInt(1, id);
                rowsDeleted = statement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
            return rowsDeleted > 0 ? 1 : 0;
        }
    }

    @Override
    public int BanUser(int id) throws SQLException {
        Users u=this.findById(id);
        if(u==null)
            return 0;
        else {
            int status;
                    if(u.isStatus()) status=0; else status=1;
            String req = "UPDATE users SET " +
                    "status = '" + status + "' " +
                    "WHERE id = " + id;
            Statement statement = cnx.createStatement();
            int rowsUpdated = statement.executeUpdate(req);
            if (rowsUpdated > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    public int admin() throws SQLException {
        int result = 0;
        String req = "SELECT COUNT(*) AS count FROM users u WHERE u.roles = ?";
        PreparedStatement statement = cnx.prepareStatement(req);
        statement.setString(1, "[\"ROLE_ADMIN\"]");
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            result = resultSet.getInt("count");
        }
        return result;
    }
}
