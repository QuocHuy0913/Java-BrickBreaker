/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wwwsh
 */
public class ValidateSignIn {
    public List<String> validateRegistration(Player player) {
        ArrayList<String> err = new ArrayList<>();
        if (player.getUsername().isEmpty()) {
            err.add("Tên người chơi không được để trống");
        } else if (player.getUsername().length() < 4) {
            err.add("Tên người chơi phải có ít nhất 4 ký tự");
        } else if (player.getUsername().length() > 10) {
            err.add("Tên người chơi phải nhỏ 10 ký tự");
        } 
        
        if(player.getPassword().isEmpty()){
            err.add("Mật khẩu không được để trống");
        } else if(player.getPassword().length() < 8){
            err.add("Mật khẩu phải có ít nhất 8 ký tự");
        } else if(player.getPassword().length() > 16){
            err.add("Mật khẩu quá dài");
        } else if(!CheckPassword(player.getPassword())){
            err.add("Mật khẩu phải bao gồm ký tự chữ và số");
        }
         
        if(player.getRePassword().isEmpty()){
            err.add("Yêu cầu xác nhận mật khẩu");
        } else if(!player.getRePassword().equals(player.getPassword())){
            err.add("Mật khẩu xác nhận không khớp với mật khẩu");
        }     
        return err;
    }
    
    public boolean CheckPassword(String pass)
    {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }
}
