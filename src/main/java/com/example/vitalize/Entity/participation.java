package com.example.vitalize.Entity;

public class participation {
    private int iduser,idevent;

    public participation() {
    }

    public participation(int iduser, int idevent) {
        this.iduser = iduser;
        this.idevent = idevent;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    @Override
    public String toString() {
        return "participation{" +
                "iduser=" + iduser +
                ", idevent=" + idevent +
                '}';
    }

    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }
}
