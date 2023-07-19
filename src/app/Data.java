package app;

import java.util.Random;

public class Data {
   double[] inputs;
   double[] values;

   public Data(double[] inputs, double[] values) {
      this.inputs = inputs;
      this.values = values;
   }

   public static Data[] generate(int amount) {
      Data[] dataset = new Data[amount];
      Random random = new Random();

      for (int i = 0; i < amount; i ++) {
         double[] inputs = { 
            random.nextDouble(-5, 5), 
            random.nextDouble(-5, 5) 
         };
         double[] values = calc(inputs); 
         dataset[i] = new Data( inputs, values );
      } return dataset;
   }

   private static double[] calc(double[] inputs) {
      double val = Math.cos(inputs[0]) * Math.sin(inputs[1]);

      if (val > 0) return new double[] {1, 0}; 
      else         return new double[] {0, 1};
   }
   @Override public String toString() {
      return "Data<["+inputs[0]+", "+inputs[1]+"] -> ["+values[0]+", "+values[1]+"]>";
   }
}