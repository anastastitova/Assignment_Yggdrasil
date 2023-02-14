package myslot;
import myslot.*;

public class Simulation {
	static Spin spin = new Spin();
	
	public static void Simulate(int num_of_games) {
		for (int i = 0; i < num_of_games; i++) {
			spin.MainSpin();
		}
	}
	
	public static void main(String[] args) {
		int num_of_games = 100000000;
		Simulate(num_of_games);
		spin.stats.PrintStat();
    }
}
