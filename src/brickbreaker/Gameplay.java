/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package brickbreaker;

import java.sql.Connection;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author wwwsh
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 30;
    private int toltalBricksStart = 0;
    private final Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int playerX1 = 520;
    private int ballPosX = 310;
    private int ballPosY = 350;
    private int ballXdir = 1;
    private int ballYdir = -2;
    private MapGenerator map;   
    private int col = 2;
    private int row = 10;
    private final JFrame obj = new JFrame();
    private Image image;
    private boolean isPaused = false;
    private int numplayer = 1;

    //History Game Variable
    Connection connection;
    public PreparedStatement st;
    private final String username;
    private final LocalDateTime timeStart;
    private LocalDateTime timeFinish;
    private Duration totalTime;
    private final String difLevel;
    
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isMovingLeft1 = false;
    private boolean isMovingRight1 = false;
    
    private Color[] bricksColors;
    
    private Color ballColor;
    
    public Gameplay(int totalBrick, int row, int col, int delay, String username, String difLevel, int numPlayer, Color[] brickColors, Color ballColor){
        
        //Set History Play
        this.username = username;
        this.timeStart = LocalDateTime.now();  
        this.difLevel = difLevel;
        
        //set Game Map
        this.totalBricks = totalBrick;
        this.toltalBricksStart = totalBrick;
        this.row = row;
        this.col = col;
        this.delay = delay;
        this.bricksColors = brickColors;
        this.ballColor = ballColor;
        
        map = new MapGenerator(this.row, this.col, this.bricksColors);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(this.delay, this);
        timer.start();
        this.numplayer = numPlayer;
        
        obj.setBounds(10, 10, 1040, 800);
        obj.setLocation(250, 10);
        obj.setTitle("Breakout ball");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        obj.add(this);
        
        // Đăng ký gameplay như là KeyListener
        obj.addKeyListener(this);

        // Bắt đầu di chuyển thanh paddle bằng luồng
        startPaddleThreads();
    }
    
    @Override
    public void paint(Graphics g)
    {
        //background
        String path = new File("src/Image/rain.jpg").getAbsolutePath();
        image = new ImageIcon(path).getImage();
        g.drawImage(image, 1, 1, 1040, 800, this);

        //draw map
        map.draw((Graphics2D)g);
        
        //borders
        g.setColor(Color.yellow);
        g.fillRect(1, 0, 3, 800);
        g.fillRect(0, 0, 1040, 3);
        g.fillRect(1023, 0, 3, 800);
        
        //username
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString("Player: "+username, 30, 30);
        
        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString("Score: "+score, 800, 30);
        
        //the paddle
        if(numplayer == 1)
        {
            g.setColor(Color.magenta);
            g.fillRect(playerX, 700, 150, 12);
        }
        
        if(numplayer == 2)
        {
            g.setColor(Color.magenta);
            g.fillRect(playerX, 700, 150, 12);
            
            g.setColor(Color.cyan);
            g.fillRect(playerX1, 700, 150, 12);
        }
       
        
        //the ball
        g.setColor(this.ballColor);
        g.fillOval(ballPosX, ballPosY, 20, 20);
        
        if(ballPosY > 800)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            
            g.setColor(new Color(255, 165, 0));
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 350, 300);
            
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Enter to restart", 400, 350);
            
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Or", 470, 400);
            
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Press Space to return to MENU", 360, 450);
        }
        
        if(score == toltalBricksStart * 10)
        {
            play = false;
            g.setColor(Color.CYAN);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("You win, Score: " + score, 390, 300);
            
            g.setFont(new Font("serif",Font.BOLD, 25));
            g.drawString("Press Enter to restart", 400, 350);
            
            g.setFont(new Font("serif",Font.BOLD, 20));
            g.drawString("Or", 500, 400);
            
            g.setFont(new Font("serif",Font.BOLD, 25));
            g.drawString("Press Space to return to MENU", 360, 450);   
            
            String path1 = new File("src/Image/congratbg.png").getAbsolutePath();
            Image congImage = new ImageIcon(path).getImage();
            g.drawImage(congImage, 7, 1, 1010, 700, this);
        }   
        
        if (isPaused) 
        {
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD, 30));
            g.drawString("Game Pause" , 420, 300);
            g.drawString("Press Space to continue", 360, 350);    
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        timer.start();
        if(play)
        {
            if(numplayer == 1)
            {
                if(new Rectangle(ballPosX,ballPosY,20,20).intersects(new Rectangle(playerX, 698, 148, 13)))
                {
                    ballYdir =- ballYdir;
                }
            }
            
            if(numplayer == 2)
            {
                Rectangle ball = new Rectangle(ballPosX,ballPosY,20,20);
                if(ball.intersects(new Rectangle(playerX1, 698, 148, 13)) || ball.intersects(new Rectangle(playerX, 698, 148, 13)))
                {
                    ballYdir =- ballYdir;
                }
            }
            
            A: for(int i = 0; i < map.map.length; i++)
            {
                for(int j = 0; j < map.map[0].length; j++)
                {
                    if(map.map[i][j] > 0)
                    {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;
                        
                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 10;
                            
                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = -ballXdir;
                            } 
                            else 
                            {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            
            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if(ballPosX <= 0)
            {
                ballXdir = -ballXdir;
            }
            if(ballPosY <= 0)
            {
                ballYdir = -ballYdir;
            }
            if(ballPosX > 1010)
            {
                ballXdir = -ballXdir;
            }
        }       
        repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }

     @Override
    public void keyReleased(KeyEvent e) {
        // Xử lý sự kiện khi có phím được nhả ra
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_RIGHT) {
            isMovingRight = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            isMovingLeft = false;
        }
        if (key == KeyEvent.VK_D) {
            isMovingRight1 = false;
        }
        if (key == KeyEvent.VK_A) {
            isMovingLeft1 = false;
        }

    }
    
    @Override
    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            isMovingRight = true;
            
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            isMovingLeft = true;
            
        }     
        //player 2
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            isMovingRight1 = true;
            
        }
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            isMovingLeft1 = true;
            
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play)
            {
                this.timeFinish = LocalDateTime.now();
                totalTime = Duration.between(timeStart, timeFinish);
                long totalTimeInSeconds = totalTime.getSeconds();

                try 
                {
                    connection = ConnectionFactory.getConnection();
                    
                    String query = "INSERT INTO history (UserName, TimeStart, TimeFinish, TotalTime, Score, DifficultLevel) VALUES (?, ?, ?, ?, ?, ?)";
                    st = connection.prepareStatement(query);
                    st.setString(1, username);
                    st.setString(2, timeStart.toString());
                    st.setString(3, LocalDateTime.now().toString());
                    st.setLong(4, totalTimeInSeconds);
                    st.setInt(5, score);
                    st.setString(6, this.difLevel);
                    st.executeUpdate();
                    connection.close();
                } 
                catch (SQLException ex) { }
                
                play = true;
                Random ranXY = new Random();
                int x = ranXY.nextInt(500);
                ballPosX = x;
                ballPosY = 350;
                ballXdir = 1;
                ballYdir = -2;        
                playerX = 310;
                score = 0;
                totalBricks = this.totalBricks;
                map = new MapGenerator(this.row, this.col, this.bricksColors);
                repaint();
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_SPACE) 
        {
            if(!play)
            {
                try 
                {
                    obj.dispose(); 
                    this.timeFinish = LocalDateTime.now();
                    totalTime = Duration.between(timeStart, timeFinish);
                    long totalTimeInSeconds = totalTime.getSeconds();
                    
                    try 
                    {
                        connection = ConnectionFactory.getConnection();
                        
                        String query = "INSERT INTO history (UserName, TimeStart, TimeFinish, TotalTime, Score, DifficultLevel) VALUES (?, ?, ?, ?, ?, ?)";
                        st = connection.prepareStatement(query);
                        st.setString(1, username);
                        st.setString(2, timeStart.toString());
                        st.setString(3, LocalDateTime.now().toString());
                        st.setLong(4, totalTimeInSeconds);
                        st.setInt(5, score);
                        st.setString(6, this.difLevel);
                        st.executeUpdate();
                        connection.close();
                    } 
                    catch (SQLException ex) {}
                    
                    MenuGame menu = new MenuGame(this.username);
                    menu.setVisible(true);
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(Gameplay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(timer.isRunning())
            {
                isPaused = true;
                timer.stop();
                repaint();
            }
            else
            {
                isPaused = false;
                timer.start();
                repaint();
            }
        }
    }

    private void startPaddleThreads() {
        Thread paddle1Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    movePaddle1();
                    try {
                        Thread.sleep(delay); // Dừng trong một khoảng thời gian để giảm tốc độ di chuyển
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        paddle1Thread.start();

        Thread paddle2Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    movePaddle2();
                    try {
                        Thread.sleep(delay); // Dừng trong một khoảng thời gian để giảm tốc độ di chuyển
                    } catch (InterruptedException e) { }
                }
            }
        });
        paddle2Thread.start();
    }
    
    private void movePaddle1() {
        if (isMovingLeft) {
            if(playerX <= 6)
            {
                playerX = 5;
            } 
            else 
            {
                play = true;
                playerX -= 5;
            }
        }

        if (isMovingRight) {
            if(playerX >= 866)
            {
                playerX = 870;
            } 
            else 
            {
                play = true;
                playerX += 5;
            }
        }
    }

    private void movePaddle2() {
        if (isMovingLeft1) {
            if(playerX1 <= 6)
            {
                playerX1 = 5;
            } 
            else 
            {
                play = true;
                playerX1 -= 5;
            }
        }

        if (isMovingRight1) {
            if(playerX1 >= 866)
            {
                playerX1 = 870;
            } 
            else 
            {
                play = true;
                playerX1 += 5;
            }
        }
    }
}
