/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;


/**
 *
 * @author wwwsh
 */
public class Player {
    private long id;
    private String username;
    private String Password;
    private String RePassword;


    public Player() {}

    public Player(String username, String Password) {
        this.username = username;
        this.Password = Password;
    }
    
    public Player(String username, String Password, String RePassword) {
        this.username = username;
        this.Password = Password;
        this.RePassword = RePassword;
    }

    
    
    public Player(long id, String username, String Password, String RePassword) {
        this.id = id;
        this.username = username;
        this.Password = Password;
        this.RePassword = RePassword;
    }

    
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getRePassword() {
        return RePassword;
    }

    public void setRePassword(String RePassword) {
        this.RePassword = RePassword;
    }
}
