package myslot;
import myslot.*;

public class Spin {
	static Boolean debug = false;
	
	enum sections{base_game, free_game};
	
	static int[][] board = {{0,0,0,0,0},
					 		{0,0,0,0,0},
					 		{0,0,0,0,0}};
	static Slot slot;
	static Statistics stats;
	
	Spin() {
		slot = new Slot();
		stats = new Statistics();
	}
	
	public static Boolean FreeSpinsTrigger() {
		int scatters = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (IsScatter(board[i][j])) {
					scatters++;
				}
			}
		}
		if (scatters >= 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static int FreeSpins() {
		int win = 0;
		for (int i = 0; i < slot.num_of_freespins; i++) {
			PullRandomBoard(sections.free_game);
			win += WinlinesLeftToRight();
		}
		return win;
	}

	public static void PullRandomBoard(sections section) {
		int point = (int) (Math.random() * slot.SectionWeight(section.ordinal()) - 50);
		if (debug) {
			System.out.println(point + " point");
		}
		int reelset = slot.GetReelset(section.ordinal(), point);
		if (debug) {
			System.out.println(reelset + " reelset");
		}
	    for (int i = 0; i < slot.board_width; i++) {
	        point = (int) (Math.random() * slot.ReelWeight(section.ordinal(), reelset, i) - 18);
	        if (debug) {
	        	System.out.println(section.ordinal() + " section");
	        	System.out.println(reelset + " reelset");
	        	System.out.println(i + " i");
	        	System.out.println(point + " point");
	        }
	        int reel_first_symbol = slot.GetReelPosition(section.ordinal(), reelset, i, point);
	        for (int j = 0; j < slot.board_height; j++) {
	        	if (section.ordinal() == 0) {
	        		board[j][i] = slot.reels_base[reelset][i][reel_first_symbol + j];
	        	}
	        	else if (section.ordinal() == 1){
	        		board[j][i] = slot.reels_fs[i][reel_first_symbol + j];
	        	}
	        	else {
	        		System.out.println("ERROR SECTION");
	        	}
	        }
	    }
	}
	
	public static int WinlinesLeftToRight() {
	    int total_win = 0;
	    int line_win;
	    int normal_symbol_win;
	    int wild_symbol_win;
	    int multiplier;
	    for (int i = 0; i < slot.number_of_winlines; ++i) {
	        line_win = 0;
	        int first_symbol = board[slot.winlines[i][0]][0];
	        int wild_subst = board[slot.winlines[i][0]][0];
	        int counter = 1;
	        int wild_counter = 0;
	        for (int j = 1; j < slot.winlines[i].length; ++j) {
	            int cur_symbol = board[slot.winlines[i][j]][j];
	            if (!IsWild(cur_symbol) && cur_symbol != first_symbol && cur_symbol != wild_subst){
	                if (IsWild(first_symbol) && IsWild(wild_subst)) {
	                    wild_subst = cur_symbol;
	                    wild_counter = counter;
	                }
	                else
	                    break;
	            }
	            ++counter;
	        }
	        normal_symbol_win = slot.paytable[wild_subst][counter-1];
	        wild_symbol_win = slot.paytable[first_symbol][wild_counter > 0 ? wild_counter -1 : 0];
	        if (normal_symbol_win == 0 && wild_symbol_win == 0)
	            continue;
	        if (normal_symbol_win >= wild_symbol_win && !IsScatter(wild_subst))
	        {
	            line_win = normal_symbol_win;
	            stats.winline_stat[wild_subst][counter - 1]++;
	        }
	        else if (!IsScatter(first_symbol) && wild_symbol_win != 0)
	        {
	            line_win = wild_symbol_win;
	            stats.winline_stat[first_symbol][wild_counter - 1]++;
	        }
	        total_win += line_win;
	    }
	    return total_win;
	}
	
	public static Boolean IsWild(int symbol) {
		return slot.wild[symbol];
	}
	
	public static Boolean IsScatter(int symbol) {
		return slot.scatter[symbol];
	}
	
	public static void CountWilds() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (IsWild(board[i][j])) {
					stats.wilds++;
					return;
				}
			}
		}
	}
	
	public static int MainSpin() {
		int win = 0;
		int fs_win = 0;
		PullRandomBoard(sections.base_game);
		CountWilds(); // for stats
		if (debug) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[i].length; j++) {
					System.out.print(board[i][j] + " ");
				}
				System.out.println();
			}
		}
		win = WinlinesLeftToRight();
		if (debug) {
			System.out.println(win + " base win");
		}
		stats.base_win += win;
		if (FreeSpinsTrigger()) {
			stats.fs_triggered++;
			fs_win = FreeSpins();
		}
		stats.fs_win += fs_win;
		win += fs_win;
		if (win != 0) {
			stats.win_spins++;
		}
		stats.common_win += win;
		stats.spins++;
		return win;
	}
}
