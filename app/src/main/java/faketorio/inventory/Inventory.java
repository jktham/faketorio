package faketorio.inventory;

import java.util.ArrayList;

import faketorio.engine.App;

public class Inventory {
	public ArrayList<ItemStack> stacks;
	public int stackSize;
	public int inventorySize;

	public Inventory() {
		stacks = new ArrayList<ItemStack>();
		stackSize = 100;
		inventorySize = 10;
	}

	public boolean canBeAdded(int id, int amount) {
		// only amount 1 supported
		if (id == -1) {
			return true;
		}
		int stackCount = 0;
		for (ItemStack stack : stacks) {
			if (stack.item.id != id || stack.amount >= stackSize) {
				stackCount += 1;
			}
		}
		if (stackCount >= inventorySize) {
			return false;
		}

		boolean found = false;
		for (ItemStack stack : stacks) {
			if (stack.item.id == id) {
				found = true;
				if (stack.amount < stackSize) {
					return true;
				}
			}
		}
		return !found;
	}

	public void addItem(int id, int amount) {
		boolean found = false;
		for (ItemStack stack : stacks) {
			if (stack.item.id == id) {
				stack.amount += amount;
				found = true;
			}
		}
		if (!found) {
			ItemStack stack = new ItemStack();
			stack.item = App.items.get(id);
			stack.amount = amount;
			stacks.add(stack);
		}
	}

	public void removeItem(int id, int amount) {
		for (ItemStack stack : stacks) {
			if (stack.item.id == id) {
				stack.amount -= amount;
				if (stack.amount <= 0) {
					stacks.remove(stack);
					break;
				}
			}
		}
	}
}
