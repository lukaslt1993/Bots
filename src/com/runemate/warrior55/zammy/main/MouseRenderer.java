package com.runemate.warrior55.zammy.main;

import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Screen;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.local.hud.InteractableRectangle;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.core.LoopingThread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.Queue;

// Note - this class is not used
public class MouseRenderer {

    private Queue<Position> positions = new LinkedList<>();
    private JPanel panel;
    private JFrame frame;
    private int tick = 0;
    private LoopingThread thread;
    private int animation = -1;
    private boolean moving = false;
    private ZammyWineGrabber bot;

    public MouseRenderer(ZammyWineGrabber bot) {
        this.bot = bot;
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.2f));
        frame.setBounds(Screen.getBounds());
        frame.setLocation(Screen.getLocation());
        frame.setUndecorated(true);
        //frame.setAlwaysOnTop( true );
        frame.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.2f));
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
        frame.add(panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.SrcOver.derive(0.05f));
                g2.fillRect(0, 0, getWidth(), getHeight());
                paintIt(g2);
            }
        });
        panel.setOpaque(false);
        panel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.2f));
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bot.stopMouseRenderer();
            }

        });
    }

    public void setThread(LoopingThread thread) {
        this.thread = thread;
    }

    public LoopingThread getThread() {
        return this.thread;
    }

    public void updatePosition() {
        if (tick++ % 500 == 0 && frame.isActive() && frame.isShowing() && frame.isFocused()) {
            InteractableRectangle rectangle = Screen.getBounds();
            if (frame.getBounds().getX() != rectangle.getX() || frame.getBounds().getY() != rectangle.getY() || frame.getBounds().getHeight() != rectangle.getHeight() || frame.getWidth() != rectangle.getWidth()) {
                frame.setBounds(Screen.getBounds());
            }
            InteractablePoint point = Screen.getLocation();
            if (frame.getLocation().getX() != point.getX() || frame.getLocation().getY() != point.getY()) {
                frame.setLocation(Screen.getLocation());
            }
        }
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public synchronized void addPosition() {
        this.animation = Players.getLocal().getAnimationId();
        this.moving = Players.getLocal().isMoving();
        MouseRenderer.Position position = new MouseRenderer.Position((int) Mouse.getPosition().getX(), (int) Mouse.getPosition().getY());
        if (positions.size() > 200) {
            positions.poll();
        }
        positions.add(position);
        panel.repaint();
    }

    private synchronized void paintIt(Graphics2D g) {
        if (positions.size() > 1) {
            int i = 0;
            Position last = null;
            g.setColor(Color.RED);
            g.setComposite(AlphaComposite.SrcOver.derive(1f));
            for (Position position : positions) {
                i++;
                if (last != null) {
                    if (i == (positions.size() - 1)) {
                        g.setStroke(new BasicStroke(15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    } else {
                        g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    }
                    g.drawLine(last.getX(), last.getZ(), position.getX(), position.getZ());
                }
                last = position;
            }
            g.drawString("Animation ID: " + animation, 20, 20);
            g.drawString("Moving: " + moving, 40, 40);
        }
    }

    public static class Position {
        
        private int x;
        private int z;

        Position(int x, int z) {
            this.x = x;
            this.z = z;
        }

        int getX() {
            return x;
        }

        int getZ() {
            return z;
        }
    }
}
