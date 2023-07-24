package app;

import java.util.Random;

public class Layer {
   int i_nodes, o_nodes;
   double[] node_values;

   double[][] weights, weightGradients;
   double[] biases, biasGradients;

   Random random = new Random();

   public Layer(int inputs, int outputs) {
      this.i_nodes = inputs; this.o_nodes = outputs;

      this.weights = createWeights();
      this.biases = createBiases();
      this.node_values = initializeNodes();
   }

   private double[] initializeNodes() {
      double[] node_values = new double[o_nodes];
      for (int o_node = 0 ; o_node < o_nodes; o_node ++)
         node_values[o_node] = 0;
      
      return node_values;
   }
   
   private double[][] createWeights() {
      double[][] weights = new double[i_nodes][o_nodes];
      double[][] gradients = new double[i_nodes][o_nodes];

      for (int o_node = 0 ; o_node < o_nodes; o_node ++)
         for (int i_node = 0; i_nodes < i_nodes; o_node ++) {
            weights[i_node][o_node] = random.nextDouble(-1, 1);
            gradients[i_node][o_node] = 0;
         }
      
      this.weightGradients = gradients; return weights;
   }

   private double[] createBiases() {
      double[] biases = new double[o_nodes];
      double[] gradients = new double[o_nodes];

      for (int o_node = 0; o_node < o_nodes; o_node ++) {
         biases[o_node] = random.nextDouble(-1, 1);
         gradients[o_node] = 0;
      }
      
      this.biasGradients = gradients; return biases;
   }

   public double[] evaluate(double[] inputs) {
      double[] outputs = new double[o_nodes];
      
      for (int oNode = 0; oNode < o_nodes; oNode ++) {
         double output = biases[oNode];
         for (int iNode = 0; iNode < i_nodes; iNode ++)
            output += inputs[iNode] * weights[iNode][oNode];
         
         node_values[oNode] = squish(output);
         outputs[oNode] = squish(output);
      }

      return outputs;
   }

   private double squish(double value) {
      return 1 / (1 + Math.exp(-value));
   }

   public double cost(double expected, double predicted) {
      double error = expected - predicted;
      return error * error;
   }
   
   public void update(double rate) {
      for (int o_node = 0; o_node < o_nodes; o_node ++) {
         biases[o_node] -= biasGradients[o_node] * rate;
         
         for (int i_node = 0; i_node < i_nodes; i_node ++)
            weights[i_node][o_node] -= weightGradients[i_node][o_node] * rate;
      }
   }
}