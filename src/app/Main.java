package app;

import data.Data;
import data.Mnist;
import data.Point;

public class Main {
   Network network;
   Data[] train_ds, test_ds;

   public static void main(String[] args) {
      int[] layers = { 784, 12, 10 };

      new Main(layers);
   }

   public Main(int[] layers) {
      Network network = new Network(layers);
      Mnist[] mnist_set = Mnist.toArray(Mnist.load());
      Point[] point_set = Point.generate(100);

      // System.out.println(mnist_set[0].inputs.length);

      network.train(mnist_set, 1000, 0.1);
      // network.display();
   }
}