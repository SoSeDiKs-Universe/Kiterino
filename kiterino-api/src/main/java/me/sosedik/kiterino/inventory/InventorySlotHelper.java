package me.sosedik.kiterino.inventory;

/**
 * Small helpers for working with inventory slots
 */
public final class InventorySlotHelper {

	private InventorySlotHelper() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Item held in cursor
	 */
	public static final int CURSOR = -1;

	/**
	 * The head inventory slot
	 */
	public static final int HEAD_SLOT = 5;

	/**
	 * The chest/chestplate inventory slot
	 */
	public static final int CHEST_SLOT = 6;

	/**
	 * The legs/leggings inventory slot
	 */
	public static final int LEGS_SLOT = 7;

	/**
	 * The feet/boots inventory slot
	 */
	public static final int FEET_SLOT = 8;

	/**
	 * The first hotbar slot
	 */
	public static final int FIRST_HOTBAR_SLOT = 36;

	/**
	 * Item held in offhand
	 */
	public static final int OFF_HAND = 45;

}
