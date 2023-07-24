package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import data.Data;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Network {
   Layer[] layers;
   int[] network;
   public Network(int[] network) {
      this.network = network;
      layers = new Layer[network.length - 1];

      for (int i = 0; i < layers.length; i ++)
         layers[i] = new Layer(network[i], network[i + 1]);
   }

   @Override public String toString() {
      String net = "Network:"+Arrays.toString(network)+":\n";
      for (int i = 0; i < layers.length; i ++) {
         Layer layer = layers[i];
         net += "layer " + i + ": { in: "+layer.i_nodes+", out: "+layer.o_nodes+" }\n";
         for (int j = 0; j < layer.node_values.length; j ++)
            net += "  - node "+i+":"+ j + " -> " + layer.node_values[i] + "\n";
      }

      return net;
   }
   
   public void display() {
      class Display extends JPanel {
         JFrame window;
         Layer[] layers;
         int diameter = 60;

         Color bg = new Color(227, 227, 227);
         Color fg = new Color(232, 164, 56);

         public Display(Network network) {
            this.layers = network.layers;
            window = new JFrame("Neural Network");
            configureDisplay();
            window.setVisible(true);
         }

         private void configureDisplay() {
            window.setSize(1920, 1080); window.add(this); 
            window.setBackground(bg);
            window.setDefaultCloseOperation(3);
            window.setLocationRelativeTo(null);
         }

         @Override public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g.setColor(fg);

            ArrayList<int[]> points = drawLayers(g2);
            ArrayList<String> values = getValues();
            g2.setFont(new Font("Verdana", 1, 16));

            for (int i = 0; i < points.size(); i ++) {
               int[] point = points.get(i); String value = values.get(i);
               g2.setColor(bg); g2.fillOval(point[0], point[1], diameter, diameter);
               g2.setColor(fg); g2.drawOval(point[0], point[1], diameter, diameter);

               g2.setColor(new Color(0, 0, 0));
               g2.drawString(value, 
                  point[0] + diameter / 4 - 4,
                  point[1] + diameter / 2 + 6
               );
            }
         }

         private ArrayList<int[]> drawLayers(Graphics2D g) {
            int width = getWidth(); int height = getHeight();

            ArrayList<int[]> points = new ArrayList<>();

            for (int col = 0; col < layers[0].i_nodes; col ++) {
               points.add( new int[] {
                  width * 1 / (2 + layers.length),
                  (height * (1 + col) / (1 + layers[0].i_nodes)),
               });
            }

            for (int line = 0; line < layers.length; line ++) {
               for (int col = 0; col < layers[line].o_nodes; col ++) {
                  points.add(new int[] {
                     width * (2 + line) / (2 + layers.length),
                     (height * (1 + col) / (1 + layers[line].o_nodes))
                  });

                  for (int a = 0; a < layers[line].i_nodes; a ++) {
                     int prev_x = width * (1 + line) / (2 + layers.length);
                     int prev_y = height * (1 + a) / (1 + layers[line].i_nodes);
      
                     g.drawLine(
                        points.get(points.size() - 1)[0]  + (diameter / 2),
                        points.get(points.size() - 1)[1]  + (diameter / 2),
                        prev_x + (diameter / 2), prev_y  + (diameter / 2)
                     );
                  }
               }
            }

            return points;
         }

         private ArrayList<String> getValues() {
            ArrayList<String> values = new ArrayList<>();

            for (int col = 0; col < layers[0].i_nodes; col ++) {
               values.add("0.00");
            }

            for (int i = 0; i < layers.length ; i ++)
               for (int j = 0; j < layers[i].node_values.length; j ++)
                  values.add(Util.round(layers[i].node_values[j], 2)+"");

            return values;
         }

      };
      new Display(this);
   }

   // Traverse the network, running the inputs through each
   // layer and updating them with each layers output
   public double[] forward(double[] inputs) {
      for (Layer layer: layers) {
         inputs = layer.evaluate(inputs);
		}
      
      return inputs;
   }

   private double cost(Data data) {
      double[] outputs = forward(data.inputs);
      Layer layer = layers[layers.length -1];
      double cost = 0;

      for (int oNode = 0; oNode < outputs.length; oNode ++)
         cost += layer.cost(outputs[oNode], data.values[oNode]);
      
      return cost;
   }

   public double cost(Data[] dataset) {
      double cost = 0;
      for (Data data: dataset) 
         cost += cost(data);

      return cost / dataset.length;
   }

	public void learn(Data[] dataset, double rate) {
      final double mod = 0.0001;
      double o_cost = cost(dataset);

      for (Layer layer: layers) {
         for (int iNode = 0; iNode < layer.i_nodes; iNode ++)
            for (int oNode = 0; oNode < layer.o_nodes; oNode ++) {
               layer.weights[iNode][oNode] += mod;
               double deltaErr = cost(dataset) - o_cost;
               layer.weights[iNode][oNode] -= mod;

               layer.weightGradients[iNode][oNode] = deltaErr / mod;
            }
         for (int idx = 0; idx < layer.biases.length; idx ++) {
            layer.biases[idx] += mod;
            double deltaErr = cost(dataset) - o_cost;
            layer.biases[idx] -= mod;
				
            layer.biasGradients[idx] = deltaErr / mod;
         }
      }

      updateLayers(rate);
   }

	private void updateLayers(double rate) {
		for (Layer layer: layers) layer.update(rate);
	}

   public double[] prettyPredict(Data data) {
      return Util.round(forward(data.inputs), 3);
   }

   public void train(Data[] dataset, int iterations, double rate) {
      for (int i = 0; i < iterations; i ++) {
         this.learn(dataset, rate);

         if (i % 500 == 0)
            System.out.println("iter[" + i + "] -> cost: " + cost(dataset));
      }
   }

   public void train(Data[] dataset, double rate, double threshhold) {
      for (int i = 0; cost(dataset) > threshhold; i ++) {
         this.learn(dataset, rate);

         if (i % 500 == 0)
            System.out.println("iter[" + i + "] -> cost: " + cost(dataset));
      }
   }
   
   public double test(Data[] dataset) {
      double score = 0;

      for (Data data: dataset)
         if (data.equals(forward(data.inputs)))
            score += 1;
      
      return score / dataset.length;
   }
}