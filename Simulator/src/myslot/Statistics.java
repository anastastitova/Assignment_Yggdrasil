package myslot;
import java.io.*;
import java.text.DecimalFormat;

public class Statistics {

	static int common_win = 0;
	static int base_win = 0;
	static int fs_win = 0;
	static int spins = 0;
	static int win_spins = 0;
	static double RTP = 0;
	static double RTP_base = 0;
	static double RTP_fs = 0;
	static int fs_triggered = 0;
	static int[][] winline_stat = new int[8][5];
	static int wilds = 0;
	
	static public void PrintStat() {
		RTP = (double)common_win / spins / myslot.Slot.stake * 100;
		RTP_base = (double)base_win / spins / myslot.Slot.stake * 100;
		RTP_fs = (double)fs_win / spins / myslot.Slot.stake * 100;
		
		try(FileWriter writer = new FileWriter("Statistics.txt", false))
        {
			writer.write("---------------------------Common stat-------------------------------\n");
			writer.write("Spin count: " + spins + "\n");
            writer.write("RTP " + new DecimalFormat("#0.0000").format(RTP) + "%\n\n");
            writer.write("RTP base section        " + new DecimalFormat("#0.0000").format(RTP_base) + "%\n");
            writer.write("RTP free games section  " + new DecimalFormat("#0.0000").format(RTP_fs) + "%\n\n");
            writer.write("Win frequency 1 in " + new DecimalFormat("#0.0000").format((double)spins / win_spins) + " spins\n");
            writer.write("Hit rate " + new DecimalFormat("#0.0000").format((double)100 / ((double)spins / win_spins)) + "%\n\n");
            writer.write("Free games 1 in " + new DecimalFormat("#0.0000").format((double)spins / fs_triggered) + " spins\n\n");
            writer.write("Wilds 1 in " + new DecimalFormat("#0.0000").format((double)spins / wilds) + " spins\n\n");
            writer.write("Winlines distribution\n");
    		for (int i = 0; i < 8; i++) {
    			writer.write(Slot.symbol_names[i] + "\t");
    			for (int j = 0; j < 5; j++) {
    				if (winline_stat[i][j] != 0) {
    					writer.write(new DecimalFormat("#0.0000").format((double)win_spins / winline_stat[i][j]) + "\t");
    				}
    				else {
    					writer.write(0 + " ");
    				}
    			}
    			writer.write("\n");
    		}
            writer.flush();
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}
}
