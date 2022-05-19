package net.minecraft.src;

import net.lax1dude.eaglercraft.EaglercraftRandom;

public class BlockLockedChest extends Block {
	protected BlockLockedChest(int par1) {
		super(par1, Material.wood);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 * Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return true;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World par1World, int par2, int par3, int par4, EaglercraftRandom par5Random) {
		par1World.setBlockToAir(par2, par3, par4);
	}

	/**
	 * When this method is called, your block should register all the icons it needs
	 * with the given IconRegister. This is the only chance you get to register
	 * icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
	}
}
