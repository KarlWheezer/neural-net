package app;

public class Network {
   Layer[] layers;
   Data[] dataset;

   public Network(int[] dimensions, Data[] dataset) {
      this.layers = new Layer[dimensions.length - 1];

      for (int i = 0; i < dimensions.length - 1; i ++)
         layers[i] = new Layer(dimensions[i], dimensions[i + 1]);

      for (int i = 0; i < layers.length; i ++) {
         Layer layer = layers[i];
         System.out.println("Layer "+i+": { in: "+layer.n_inputs+", out: "+layer.n_outputs + " }");
      }
   }

   // Traverse the network, running the inputs through each
   // layer and updating them with each layers output
   double[] forward(double[] inputs) {
      for (Layer layer: layers)
         inputs = layer.evaluate(inputs);
      
      return inputs;
   }

   String classify(double[] outputs, String[] values) {
      outputs = forward(outputs); int idx = 0;
      for (int i = 0; i < outputs.length; i++)
         idx = outputs[i] > outputs[idx] ? i : idx;

      return values[idx];
   }

   public String toString() {
      String str = "";
      for (int i = 0; i < layers.length; i ++) {
         Layer layer = layers[i];
         str.concat("Layer "+i+": { in: "+layer.n_inputs+", out: "+layer.n_outputs + " }");
      }

      return str;
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
         for (int iNode = 0; iNode < layer.n_inputs; iNode ++)
            for (int oNode = 0; oNode < layer.n_outputs; oNode ++) {
               layer.weights[iNode][oNode] += mod;
               double deltaErr = cost(dataset) - o_cost;
               layer.weights[iNode][oNode] -= mod;

               layer.weightGradient[iNode][oNode] = deltaErr / mod;
            }
         for (int idx = 0; idx < layer.biases.length; idx ++) {
            layer.biases[idx] += mod;
            double deltaErr = cost(dataset) - o_cost;
            layer.biases[idx] -= mod;

            layer.biasGradient[idx] = deltaErr / mod;
         }
      }

      updateAllLayers(rate);
   }

   public void train(boolean debug, double rate, int iters) {
      if (debug) {
         for (int i = 0; i < iters; i ++) {
            learn(dataset, rate);
            if (i % 1000 == 0) {
               double cost = App.round(cost(dataset), 4);
               int col = 0;
               if (cost > 0.3) col = 31;
               if (cost < 0.3 && cost > 0.2) col = 33;
               else if (cost < 0.2) col = 32;
               System.out.println("Iteration: " + i + ", Cost: \u001B["+col+"m" + cost + "\u001B[0m");
			   }
		   }
      } else for (int i = 0; i < iters; i ++) learn(dataset, rate);
   }

   private void updateAllLayers(double rate) {
      for (Layer layer: layers) layer.update(rate);
   }
}