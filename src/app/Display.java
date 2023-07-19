package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {
   Network network;
   Layer[] layers;
   JFrame window;
   Color gray = new Color(59, 58, 58);
   Color white = new Color(255, 255, 255);
   int nodeRad = 50;

   public Display(Network network) {
      this.network = network;
      this.layers = network.layers;
      this.window = new JFrame("Neural Network");

      this.configureDisplay();
      window.setVisible(true);
   }

   private void configureDisplay() {
      window.setSize(1600, 900);
      window.add(this);
      window.setDefaultCloseOperation(3);
      window.setLocationRelativeTo(null);
      window.setBackground(new Color(59, 58, 58));
   }

   @Override public void paint(Graphics g) {
      final int width = getWidth();
      final int height = getHeight();
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(5));

      ArrayList<int[]> points = new ArrayList<>();
      g.setColor(new Color(255, 255, 255));

      for (int i = 0; i < layers[0].n_inputs; i ++) {
         int x = width  / (2 + layers.length);
         int y = height * (1 + i) / (1 + layers[0].n_inputs);

         points.add(new int[] { x, y - (nodeRad / 2)});
      }

      for (int line = 0; line < layers.length; line ++) {
         for (int col = 0; col < layers[line].n_outputs; col ++) {
            int x = width * (2 + line) / (2 + layers.length);
            int y = (height * (1 + col) / (1 + layers[line].n_outputs));
            points.add(new int[] { x, y - (nodeRad / 2) });

            String msg = "Bias: " + App.round(layers[line].biases[col], 3);

            g2.drawString(msg, x, y + - 2 * nodeRad);
            for (int a = 0; a < layers[line].n_inputs; a ++) {
               int prev_x = width * (1 + line) / (2 + layers.length);
               int prev_y = height * (1 + a) / (1 + layers[line].n_inputs);

               g2.drawLine(x + (nodeRad / 2), y, prev_x + (nodeRad / 2), prev_y);
            }
         }
      }

      for (int[] point: points) {
         g.setColor(new Color(59, 58, 58));
         g.fillOval(point[0], point[1], nodeRad, nodeRad);
         g.setColor(new Color(255, 255, 255));
         g.drawOval(point[0], point[1], nodeRad, nodeRad);
      }
   }
}