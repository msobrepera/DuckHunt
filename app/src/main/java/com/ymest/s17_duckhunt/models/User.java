package com.ymest.s17_duckhunt.models;

public class User {
    private String nick;
    private int ducks;
    private int tipopartida;

    public User(){

    }

    public User(String nick, int ducks, int tipopartida) {
        this.nick = nick;
        this.ducks = ducks;
        this.tipopartida = tipopartida;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getDucks() {
        return ducks;
    }

    public void setDucks(int ducks) {
        this.ducks = ducks;
    }

    public int getTipoPartida() {
        return tipopartida;
    }

    public void setTipoPartida(int tipopartida) {
        this.tipopartida = tipopartida;
    }
}
