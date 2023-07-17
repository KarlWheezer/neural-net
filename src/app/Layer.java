package app;
public class Layer {
   int n_inputs, n_outputs;
   double[][] weights;
   double[] biases;

   public Layer(int n_inputs, int n_outputs) {
      this.n_inputs = n_inputs;
      this.n_outputs = n_outputs;

      weights = new double[n_inputs][n_outputs];
      biases = new double[n_outputs];
   }

   public double[] calculate(double[] inputs) {
      double[] outputs = new double[n_outputs];

      for (int i = 0; i < n_outputs; i ++) {
         double output = biases[i];
         for (int j = 0; j < n_inputs; j ++) {
            output += inputs[j] * weights[j][i];
         }
         outputs[i] = output;
      }

      return outputs;
   }
}