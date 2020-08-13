package ga.ejer.doom;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

public class Game extends JFrame implements Runnable{
    private static final long serialVersionUID = 1L;
    public int mapWidth = 15;
    public int mapHeight = 15;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public ArrayList<Texture> textures;
    public Camera camera;
    public Screen screen;
    public String status;
    public static int[][] map =
            {
                    {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,1,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,0,0,0,1,0,0,0,0,0,0,0,0,0,4},
                    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
                    {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
            };

    //    P - Player spawn spot
    //
    //    {1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
    // P  {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},       ||
    // -> {1,0,0,0,P,0,0,0,0,0,0,0,0,0,2},       ||     Player spawns facing
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},      _||_    downwards on the map
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2},      \  /
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},       \/
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
    //    {1,0,0,0,1,0,0,0,0,0,0,0,0,0,4},
    //    {1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
    //    {1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}

    public Game() {
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        textures = new ArrayList<Texture>();
        textures.add(Texture.wood);
        textures.add(Texture.brick);
        textures.add(Texture.bluestone);
        textures.add(Texture.stone);
        camera = new Camera(4.5, 4.5, 1, 0, 0, -.66);
        screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);
        addKeyListener(camera);
        setSize(640, 480);
        setResizable(false);
        setTitle("Doom Engine (Java)");
        Image icon = new ImageIcon("res/icon.png").getImage();
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setVisible(true);
        status = "Status Text";
        start();
    }
    private synchronized void start() {
        running = true;
        thread.start();

        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("res/e1m1.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage img = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(image, 0, 0, w, h, this);
        g2d.setPaint(Color.red);

        try {
            Font doomFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/DooM.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(doomFont);

            g2d.setFont(doomFont);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(status, 3, 43);
        g2d.dispose();

        g.drawImage(img, 0, 0, image.getWidth(), image.getHeight(), null);

        bs.show();
    }
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
                //handles all of the logic restricted time
                screen.update(camera, pixels);
                camera.update(map);
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }
    public static void main(String [] args) {
        Game game = new Game();
    }
}