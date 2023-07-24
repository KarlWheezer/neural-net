package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Mnist extends Data {
   public double[] values;
   public double[] inputs;

   public static ArrayList<Mnist> load() {
      File file = new File("data/train.csv");
      ArrayList<Mnist> dataset = new ArrayList<>();
      String line = "";

      try {
         BufferedReader br = new BufferedReader(new FileReader(file));
         br.readLine();

         while ((line = br.readLine()) != null) {
            String[] strs = line.split(",");
            double[] list = new double[strs.length];

            for (int i = 0; i < strs.length; i ++)
               list[i] = Double.valueOf(strs[i]);
            
            dataset.add(new Mnist(Arrays.copyOfRange(list, 1, list.length), labelToValues((int) list[0])));
         }

         br.close();
      } catch (IOException e) { e.printStackTrace(); }
      
      return dataset;
   }
   public static Mnist[] toArray(ArrayList<Mnist> dataset) {
      Mnist[] data = new Mnist[dataset.size()];

      for (int i = 0; i < dataset.size(); i ++)
         data[i] = dataset.get(i);

      return data;
   }
   public static<T> T[] subArray(T[] array, int beg, int end) {
      return Arrays.copyOfRange(array, beg, end + 1);
   }
   private static double[] labelToValues(int input) {
      double[] arr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      arr[input] = 1;
      return arr;
   }

   public Mnist(double[] inputs, double[] values) {
      super(inputs, values);
      this.inputs = inputs;
      this.values = values;
   }

   @Override public String toString() {
      return "Mnist {\n   inputs: [" + this.inputs[0] + ", " + this.inputs[1] + ", ... "+this.inputs[this.inputs.length - 2]+ ", " + this.inputs[this.inputs.length - 1] + "],\n   values: "+Arrays.toString(values)+"\n}";
   }
}