package app;

public class Network {
   Layer[] layers;
   int[] dimensions;

   public Network(int[] dimensions) {
      this.dimensions = dimensions;
      this.layers = new Layer[dimensions.length];

      this.layers[0] = new Layer(dimensions[0], dimensions[0]);
      for (int i = 1; i < this.dimensions.length; i ++) {
         layers[i] = new Layer(this.dimensions[i -1], this.dimensions[i]);
      }
   }

   public double[] forward(double[] inputs) {
      for (Layer layer: layers)
         inputs = layer.evaluate(inputs);
      return inputs;
   }

   private double cost(Data data) {
      double[] outputs = forward(data.inputs);
      Layer outputLayer = layers[layers.length-1];
      double cost = 0;

      for (int i = 0; i < outputs.length-1; i ++) {
         cost += outputLayer.cost(outputs[i], data.values[i]);
      }

      return cost;
   }

   public double cost(Data[] dataset) {
      double cost = 0;

      for (Data data: dataset) 
         cost += cost(data);

      return cost / dataset.length;
   }

   public void train(Data[] dataset, double rate) {
      final double mod = 0.00001;
      double initialCost = cost(dataset);

      // for each node per layer, slightly modify each weight to see how it effects the cost 
      for (Layer layer: layers) {
         for (int i = 0; i < layer.n_outputs; i ++) for (int j = 0; j < layer.n_inputs; j ++) {
            layer.weights[j][i] += mod;
            double cost = cost(dataset) - initialCost;
            layer.weights[j][i] -= mod;
            layer.weightGradient[j][i] = cost / mod;
         }

         for (int j = 0; j < layer.biases.length -1; j ++) {
            layer.biases[j] += mod;
            double cost = cost(dataset) - initialCost;
            layer.biases[j] -= mod;
            layer.biasGradient[j] = cost / mod;
         }
      }

      updateLayers(rate);
   }

   private void updateLayers(double rate) {
      for (Layer layer: layers) layer.update(rate);
   }

   public void display() {
      new Display(this.layers);
   }
}