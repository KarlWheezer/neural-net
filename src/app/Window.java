package app;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Window extends JPanel {
   int[] layers;
   JFrame win;

   public Window(int[] layers) {
      this.win = new JFrame("Nueral Network");
      win.setDefaultCloseOperation(3);
      win.setSize(1000, 750);
      win.setBackground(new Color(59, 58, 58));

      this.layers = layers;
      
      win.setLocationRelativeTo(null);      
      win.add(this); win.setVisible(true); 
   }
   
   public void paint(Graphics g) {
      int width = getWidth();
      int height = getHeight();

      ArrayList<int[]> points = new ArrayList<>();

      for (int i = 0; i < layers.length; i ++) {
         for (int j = 0; j < layers[i]; j ++) {
            int x = width * (1 + i) / (1 + layers.length) - 30;
            int y = height * (1 + j) / (1 + layers[i]) - 30;

            if (i != 0) for (int a = 0; a < layers[i -1]; a ++) {
               int prev_x = width * i / (1 + layers.length) - 5;
               int prev_y = height * (1 + a) / (1 + layers[i - 1]) - 5;

               Graphics2D g2 = (Graphics2D) g;
               g2.setColor(new Color(67, 224, 120));
               g2.setStroke(new BasicStroke(5));

               g2.drawLine(x+25, y+25, prev_x, prev_y);
            }
            points.add(new int[] {x, y});
            g.setColor(new Color(28, 28, 28));
         }
      }
      for (int[] point: points) {
         g.fillOval(point[0], point[1], 50, 50);
      }
   }
}