package app;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class App {
	public static void main(String[] args) throws Exception {
		int[] dimensions = {2, 16, 2};
		Data[] datasset = Data.generate(100);

		Network network = new Network(dimensions, datasset);
		System.out.println(network);

		// network.train(true, 0.01, 100_000);
		new Display(network);
	}
	
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}