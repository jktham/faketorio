package faketorio.inventory;

import java.util.ArrayList;

public class Recipe {
	public ArrayList<ItemStack> input;
	public ArrayList<ItemStack> output;
	public int maxInputAmount;
	public int time;

	public Recipe(ArrayList<ItemStack> input, ArrayList<ItemStack> output, int maxInputAmount, int time) {
		this.input = input;
		this.output = output;
		this.maxInputAmount = maxInputAmount;
		this.time = time;
	}
}
