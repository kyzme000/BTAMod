package kuzme.kaifcraft.items;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import java.util.Random;

public class ItemBlunt extends Item {

	public int reloadTimer;

	public final Random random = new Random();


	public ItemBlunt(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		Vec3 plylook = entityplayer.getLookAngle();
		double spawnX = entityplayer.x + plylook.x * 0.5;
		double spawnY = entityplayer.y + plylook.y * 0.5;
		double spawnZ = entityplayer.z + plylook.z * 0.5;
		double dx = plylook.x * 2;
		double dy = plylook.y * 0.5;
		double dz = plylook.z * 2;

		if (this.reloadTimer == 0) {
			int i;
			for (i=0; i<=3; i++) {
				world.spawnParticle(
					"bigsmoke", spawnX, spawnY, spawnZ, dx, dy, dz, 2
				);
				world.spawnParticle(
					"largesmoke", spawnX, spawnY, spawnZ, dx, dy, dz, 2
				);
			}

			world.playSoundAtEntity(entityplayer, entityplayer, "kaifcraft:cough", 0.45F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F);
			//this.reloadTimer = 50;
		}
		return itemstack;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
		if(this.reloadTimer > 0) {
			this.reloadTimer--;
		}
	}


}
