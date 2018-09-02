package tk.nukeduck.vivivox.entity.inventory;

public class Inventory {
	private Stack[] slots;
	
	public Inventory(int slots) {
		this.slots = new Stack[slots];
	}
	
	public Stack getStackInSlot(int i) {
		return i < slots.length && i >= 0 ? slots[i] : new Stack();
	}
}