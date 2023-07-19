package app;

public class App {
	public static void main(String[] args) throws Exception {
		int[] dimensions = { 2, 3, 1 };
		Network network = new Network(dimensions);

		Data[] dataset = new Data[] {
			new Data(new double[]{1, 1}, new double[]{0}),
			new Data(new double[]{0, 1}, new double[]{1}),			
			new Data(new double[]{1, 0}, new double[]{1}),			
			new Data(new double[]{0, 0}, new double[]{0}),
		};

		double o = network.cost(dataset);

		for (int i = 0; i < 1000; i ++) {
			network.train(dataset, 0.001);

			if (i % 50 == 0)
				System.out.println(String.format("Network[iteration: %d, cost: %f ]", i, network.cost(dataset)));
		}
		System.out.println(String.format("Network[iteration: 0, cost: %f ]", o));

		network.display();
	}
}