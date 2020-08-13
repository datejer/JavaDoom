package ga.ejer.doom;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Camera implements KeyListener{
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    public boolean left, right, forward, back;
    public final double MOVE_SPEED = .07;
    public float Xaccelf;
    public float Yaccelf;
    public float Xaccelb;
    public float Yaccelb;
    public final double ROTATION_SPEED = .05;

    public Camera(double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }

    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;
    }

    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }

    public void update(int[][] map) {
        if(forward) {
            if (Xaccelf < 0.1) Xaccelf += 0.005;
            if (Yaccelf < 0.1) Yaccelf += 0.005;
            if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) xPos+=xDir*Xaccelf;
            if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] ==0) yPos+=yDir*Yaccelf;
        }
        if(!forward) {
            if (Xaccelf >= 0.005) Xaccelf -= 0.005;
            else Xaccelf = 0;
            if (Yaccelf >= 0.005) Yaccelf -= 0.005;
            else Yaccelf = 0;
            if (map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) xPos+=xDir*Xaccelf;
            if (map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] ==0) yPos+=yDir*Yaccelf;
        }
        if(back) {
            if (Xaccelb < 0.1) Xaccelb += 0.005;
            if (Yaccelb < 0.1) Yaccelb += 0.005;
            if (map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0) xPos-=xDir*Xaccelb;
            if (map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]==0) yPos-=yDir*Yaccelb;
        }
        if(!back) {
            if (Xaccelb >= 0.005) Xaccelb -= 0.005;
            else Xaccelb = 0;
            if (Yaccelb >= 0.005) Yaccelb -= 0.005;
            else Yaccelb = 0;
            if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0) xPos-=xDir*Xaccelb;
            if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]==0) yPos-=yDir*Yaccelb;
        }
        if(right) {
            double oldxDir=xDir;
            xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
            yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
        }
        if(left) {
            double oldxDir=xDir;
            xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
            yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
        }
    }

    public void keyTyped(KeyEvent arg0) {

    }
}
