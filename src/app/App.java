package app;
public class App {
    public static void main(String[] args) throws Exception {
        Network network = new Network(new int[] {2, 4, 4, 2});
        network.display();
    }
}