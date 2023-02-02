package faketorio.inventory;

public class ItemStack {
	public Item item;
	public int amount;

	public ItemStack() {
		item = new Item();
		amount = 0;
	}

	public ItemStack(Item item, int amount) {
		this.item = item;
		this.amount = amount;
	}
}
