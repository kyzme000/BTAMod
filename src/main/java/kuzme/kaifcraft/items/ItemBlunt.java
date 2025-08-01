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
		entityplayer.sendMessage("world.isClientSide = " + world.isClientSide);

		Vec3 plylook = entityplayer.getLookAngle();
		Vec3 right = plylook.crossProduct(Vec3.getTempVec3(0, 1, 0)).normalize();


		if (this.reloadTimer == 0) {
			int count = 3;
			double spacing = 0.6;
			double speed = 0.05;

			for (int i = 0; i < count; i++) {
				double offsetIndex = i - (count - 1) / 2.0;

				double offsetX = right.x * offsetIndex * spacing;
				double offsetY = right.y * offsetIndex * spacing;
				double offsetZ = right.z * offsetIndex * spacing;

				double spawnX = entityplayer.x + plylook.x * 0.5 + offsetX;
				double spawnY = entityplayer.y + plylook.y * 0.5 + offsetY;
				double spawnZ = entityplayer.z + plylook.z * 0.5 + offsetZ;

				Vec3 direction = entityplayer.getLookAngle().normalize();

				double dx = direction.x * speed;
				double dy = direction.y * speed;
				double dz = direction.z * speed;

				world.spawnParticle("bigsmoke", spawnX, spawnY, spawnZ, dx, dy, dz, 2);
			}

			world.playSoundAtEntity(entityplayer, entityplayer, "kaifcraft:cough", 0.45F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F);
			this.reloadTimer = 5;
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
