import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceImpact extends JPanel implements ActionListener, KeyListener {
    int playerY = 200;
    ArrayList<Rectangle> bullets = new ArrayList<>();
    ArrayList<Rectangle> enemies = new ArrayList<>();
    int enemySpawnTimer = 0;

    boolean movingUp = false;
    boolean movingDown = false;

    boolean shooting = false;
    int shootCooldown = 0;

    Image playerRocketImg;
    Image bulletImg;
    Image enemyRocketImg;
    Image backgroundImg;

    SpaceImpact() {
        Timer timer = new Timer(16, this); // 60 FPS
        timer.start();
        setFocusable(true);
        addKeyListener(this);

        playerRocketImg = new ImageIcon("player_rocket.png").getImage();
        bulletImg = new ImageIcon("bullet.png").getImage();
        enemyRocketImg = new ImageIcon("enemy_rocket.png").getImage();
        backgroundImg = new ImageIcon("background.png").getImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

       //background
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);

        // Draw Player
        g.drawImage(playerRocketImg, 50, playerY, 300, 300, this);

        // Draw Bullets
        for (Rectangle b : bullets) {
            g.drawImage(bulletImg, b.x, b.y, 30, 15, this);
        }

        // Draw Enemies
        for (Rectangle e : enemies) {
            g.drawImage(enemyRocketImg, e.x, e.y, 200, 200, this);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (movingUp) playerY -= 5;
        if (movingDown) playerY += 5;

        if (shooting) {
            if (shootCooldown == 0) {
                bullets.add(new Rectangle(350, playerY + 142, 30, 15));
                shootCooldown = 15; 
            }
        }

        if (shootCooldown > 0) {
            shootCooldown--; 
        }

        // Move bullets -> right
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).x += 7;
        }

        // Move enemies -> left
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).x -= 3;
        }

        // Spawn enemy
        enemySpawnTimer++;
        if (enemySpawnTimer > 100) {
            enemies.add(new Rectangle(1800, (int)(Math.random() * 450), 350, 350));
            enemySpawnTimer = 10;
        }

        // Check collision bullet vs enemy
        for (int i = bullets.size()-1; i >=0; i--) {
            for (int j = enemies.size()-1; j >=0; j--) {
                if (bullets.get(i).intersects(enemies.get(j))) {
                    bullets.remove(i);
                    enemies.remove(j);
                    break;
                }
            }
        }

        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) movingUp = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) movingDown = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) movingUp = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) movingDown = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = false;
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame f = new JFrame("Space Impact - Java");
        f.add(new SpaceImpact());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}