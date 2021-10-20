import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

public class Game implements ActionListener, KeyListener{

    public static final int FPS = 60, WIDTH = 1600, HEIGHT = 900;
    public static int highscore = 0;
    private Bird bird;
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private int time, scroll;

    private boolean paused;

    public void go() {
        frame = new JFrame("Flappy Bird");
        bird = new Bird();
        rects = new ArrayList<>();
        panel = new Panel(this, bird, rects);
        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(this);
        Panel.bg = new Color(95, 105, 171);
        Panel.Pipes = new Color(45, 197, 73);

        paused = false;

        Timer t = new Timer(500 / FPS, this);
        t.start();
    }


    public static void main(String[] args) {
        JFrame frame = new Menu("Crappy Bird");
        frame.setVisible(true);

    }
    public void actionPerformed(ActionEvent e) {


        panel.repaint();
        if(!paused) {
            bird.physics();
            if(scroll % 90 == 0) {
                int h1 = (int) ((Math.random()*HEIGHT)/5 + (0.3*Math.random())*HEIGHT);
                int h2 = HEIGHT/(3+time/2000);
                Rectangle r = new Rectangle(WIDTH, 0, Panel.PIPE_W, h1);
                Rectangle r2 = new Rectangle(WIDTH, h1+h2, Panel.PIPE_W, HEIGHT-(h1+h2));
                rects.add(r);
                rects.add(r2);
            }
            ArrayList<Rectangle> toRemove = new ArrayList<>();
            boolean game = true;
            for(Rectangle r : rects) {
                r.x-=3;
                if(r.x + r.width <= 0) {
                    toRemove.add(r);
                }
                if(r.contains(bird.x, bird.y)){
                    game = false;
                }
            }
            rects.removeAll(toRemove);
            time++;
            scroll++;

            if(bird.y > HEIGHT || bird.y+ Bird.RAD < 0) {
                game = false;
            }

            if(!game) {
                if ((time/10) > highscore){
                    highscore = time/10;
                }
                paused = true;
            }
        }
    }

    public void ScoreboardWriter() throws IOException {
        File myObj = new File("Scoreboard.txt");
        FileWriter myWriter = new FileWriter(myObj);
        myWriter.write(time/10);
        myWriter.close();
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
        }
        myReader.close();
    }
    public void Scoreboard(){

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            bird.up();
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            bird.down();
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE){
            if(paused) {
                frame.dispose();
                JFrame frame = new Menu("Crappy Bird");
                frame.setVisible(true);
                rects.clear();
                bird.reset();
                time = 0;
                scroll = 0;
                paused = false;
            }
        }
    }

    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) {}
    public int getScore() {
        return Math.max(time / 10 - 40, 0);
    }
    public boolean paused() {
        return paused;
    }
}