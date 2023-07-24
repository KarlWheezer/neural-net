package data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import app.Util;

public class Point extends Data {
   public double[] inputs;
   public double[] values;

   public Point(double[] inputs, double[] values) {
      super(inputs, values);
   }

   @Override public String toString() {
      return "Data { inputs: "+Arrays.toString(Util.round(inputs, 3))+", values: "+Arrays.toString(values)+" }";
   }

   public static Point[] generate(int amount) {
      Point[] dataset = new Point[amount];
      Random random = new Random();

      for (int i = 0; i < amount; i ++) {
         double[] inputs = {
            random.nextDouble(-1, 1),
            random.nextDouble(-1, 1),
         };
         double[] values = classify(inputs);

         dataset[i] = new Point(inputs, values);
      }

      return dataset;
   }

   private static double[] classify(double[] inputs) {
      double val = func(inputs); 

      if (val > 0) return new double[] {0, 1}; 
      else return new double[] {1, 0};
   }

   private static double func(double[] inputs) {
      double x = inputs[0]; double y = inputs[1];
      return 1 / (2 * x + 0.5 * y);
      // return x / 2 * y;
      // return 1 / ( x + y );
      // return 2 * Math.sin(x) - 30 * Math.sin(y);
   }

   public static void display(Data[] dataset) {
      class Display extends JPanel {
         Data[] dataset; JFrame window;
   		Color bg = new Color(227, 227, 227);

         public Display(Data[] dataset) {
            this.dataset = dataset;
            window = new JFrame("Data");
            configureDisplay(); 
            window.setVisible(true);
         }

         private void configureDisplay() {
            window.setSize(1920, 1080); 
            window.add(this); 
            window.setBackground(bg);
            window.setDefaultCloseOperation(3);
            window.setLocationRelativeTo(null);
         }

         @Override public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));

            int height = getHeight() / 2; 
            int width = getWidth() / 2;

            g2.setColor(new Color(130, 130, 130));
            g2.drawLine(0, height, width * 2, height);
            g2.drawLine(width, 0, width, height * 2);

            g2.setStroke(new BasicStroke(2));
            for (Data data: dataset) {
               Color color = new Color(65, 204, 102);
               if (data.values[0] == 0) 
                  color = new Color(227, 59, 59);

               g2.setColor(color);
               g2.drawOval(
                  width + (int) (data.inputs[0] * (width - 1)), 
                  height + (int) (data.inputs[1] * (height - 1)), 
               7, 7);
            }
         }
      }; new Display(dataset);
   }

   public boolean equals(double[] predictions) {
      if (predictions[0] > 0.75 && values[0] == 1) return true;
      if (predictions[1] > 0.75 && values[1] == 1) return true;
      
      return false;
   }
}