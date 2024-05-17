package com.example.vitalize.Service;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceInterface <entity>{
    public entity login(entity t) throws SQLException;
    public int registre(entity t) throws SQLException;
    public List<entity> getAllusers() throws SQLException;
    public entity findById(int id) throws SQLException;
    public entity findByEmail(String email) throws SQLException;
    public entity ModifierUser(entity t,String email) throws SQLException;
    public int supprimerUser(int id) throws SQLException;
    public int BanUser(int id) throws SQLException;
    public int ResponseRequestMedecin(int id,boolean response) throws SQLException;
    public int SendVerifCode(String email) throws SQLException;
    public int ResetPassword(String email ,String verif,String password) throws SQLException;
    public List<entity> getAllMedecinRequest() throws SQLException;
}
