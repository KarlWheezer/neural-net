package app;

import java.util.Random;

public class Layer {
   int n_inputs, n_outputs;
   double[][] weights, weightGradient;
   double[] biases, biasGradient;

   public Layer(int n_inputs, int n_outputs) {
      this.n_inputs = n_inputs;
      this.n_outputs = n_outputs;

      weightGradient = new double[n_inputs][n_outputs];
      biasGradient  = new double[n_outputs];

      this.weights = initializeWeights();
      this.biases  = new double[n_outputs];
   }

   public double[] evaluate(double[] inputs) {
      double[] outputs = new double[n_outputs];

      for (int i = 0; i < n_outputs; i ++) {
         double output = biases[i];
         for (int j = 0; j < n_inputs; j ++) {
            output += weights[j][i] * inputs[j];
         }
         outputs[i] = squish(output);
      }

      return outputs;
   }

   public void update(double rate) {
      for (int i = 0; i < n_outputs; i ++) {
         biases[i] -= biasGradient[i] * rate;
         for (int j = 0; j < n_inputs; j ++) {
            weights[j][i] = weightGradient[j][i] * rate;
         }
      }
   }

   private double[][] initializeWeights() {
      Random rand = new Random();
      double[][] weights = new double[n_inputs][n_outputs];
      for (int i = 0; i < n_outputs; i ++) for (int j = 0; j < n_inputs; j ++)
         weights[j][i] = rand.nextDouble(-1, 1) / Math.sqrt(n_inputs);

      return weights;
   }
   private double[] initializeBiases() {
      Random rand = new Random();
      double[] biases = new double[n_outputs];
      for (int j = 0; j < n_outputs; j ++)
         biases[j] = rand.nextDouble(-1, 1) / Math.sqrt(n_outputs);
      
      return biases;
   }


   private double squish(double value) {
      return 1 / ( 1 + Math.exp(-value));
   }

   public double cost(double predicted, double expected) {
      double error = predicted - expected;
      return error * error;
   }
}