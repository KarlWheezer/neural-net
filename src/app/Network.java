package app;

public class Network {
   Layer[] layers;
   int[] dimensions;
   public Network(int[] dimensions) {
      this.dimensions = dimensions;
      this.layers = new Layer[dimensions.length];

      for (int i = 0; i < dimensions.length -1; i ++) {
         this.layers[i] = new Layer(dimensions[i], dimensions[i + 1]);
      }
   }

   public void display() {
      new Window(this.dimensions);
   }
}