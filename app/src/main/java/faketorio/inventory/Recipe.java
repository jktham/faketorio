package faketorio.inventory;

import java.util.ArrayList;

public class Recipe {
	public ArrayList<ItemStack> input;
	public ArrayList<ItemStack> output;

	public Recipe(ArrayList<ItemStack> input, ArrayList<ItemStack> output) {
		this.input = input;
		this.output = output;
	}
}
