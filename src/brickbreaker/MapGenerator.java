/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author wwwsh
 */
public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    private Color[] brickColors;
    
     public MapGenerator(int row, int col, Color[] bricksColors){
        this.brickColors = bricksColors;
        map = new int[row][col];
        for (int[] map1 : map) {
            for (int j = 0; j < map[0].length; j++) {
                map1[j] = 1;
            }
        }
         
         brickWidth = 850/col;
         brickHeight = 150/row;
     }
     
     public void draw(Graphics2D g){
         Random random = new Random();
         
         for(int i = 0; i < map.length; i++){
             for(int j = 0; j < map[0].length; j++){
                 if(map[i][j] > 0){
                     
                     int randomColorIndex = random.nextInt(brickColors.length);
                     Color randomColor = brickColors[randomColorIndex];
                     
                     g.setColor(randomColor);
                     g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                     g.setStroke(new BasicStroke(3));
                     g.setColor(Color.black);
                     g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                 }
             }
        }
     }
     
     public void setBrickValue(int value, int row, int col){
         map[row][col] = value;
     }
}
