package app;

import java.util.Random;

public class Layer {
   int n_inputs, n_outputs;
   double[][] weights, weightGradient;
   double[]   biases,  biasGradient;

   public Layer(int n_inputs, int n_outputs) {
      this.n_inputs = n_inputs;
      this.n_outputs = n_outputs;

      weightGradient = new double[n_inputs][n_outputs];
      biasGradient  = new double[n_outputs];

      this.weights = randomizeWeigts();
      this.biases  = randomizeBiases();
   }

   private double[][] randomizeWeigts() {
      double[][] n_weights = new double[n_inputs][n_outputs];
      Random random = new Random();

      for (int oNode = 0; oNode < n_outputs; oNode ++)
         for (int iNode = 0; iNode < n_inputs; iNode ++)
            n_weights[iNode][oNode] = random.nextDouble(-1, 1) / Math.sqrt(n_inputs);
      
      return n_weights;
   }

   private double[] randomizeBiases() {
      double[] n_biases = new double[n_outputs];
      Random random = new Random();

      for (int oNode = 0; oNode < n_outputs; oNode ++)
         n_biases[oNode] = random.nextDouble(-1, 1) / Math.sqrt(n_inputs);
      
      return n_biases;
   }

   public double[] evaluate(double[] inputs) {
      double[] outputs = new double[n_outputs];
      
      for (int oNode = 0; oNode < n_outputs; oNode ++) {
         double output = biases[oNode];
         for (int iNode = 0; iNode < n_inputs; iNode ++)
            output += inputs[iNode] * weights[iNode][oNode];

         outputs[oNode] = squish(output);
      }

      return outputs;
   }

   public double cost(double expected, double predicted) {
      double error = expected - predicted;
      return error * error;
   }

   private double squish(double value) {
      return 1 / ( 1 + Math.exp(-value));
   }

   public void update(double rate) {
      for (int oNode = 0; oNode < n_outputs; oNode ++) {
         biases[oNode] -= biasGradient[oNode] * rate;
         for (int iNode = 0; iNode < n_inputs; iNode ++)
            weights[iNode][oNode] -= weightGradient[iNode][oNode] * rate;
      }
   }
}