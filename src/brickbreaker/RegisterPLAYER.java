/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author wwwsh
 */
public class RegisterPLAYER {
    Connection con = null;
    PreparedStatement ps = null;
    int st;

    public int registerPlayer(Player player) {
        con = ConnectionFactory.getConnection();
        try {
            String query = "insert into "
                    + "player "
                    + "(UserName, Password) "
                    + "value(?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getPassword() );
            st = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            st = -2;
        }
        return st;
    }
}
