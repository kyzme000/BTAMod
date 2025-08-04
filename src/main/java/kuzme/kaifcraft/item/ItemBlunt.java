package kuzme.kaifcraft.item;


import kuzme.kaifcraft.entity.EntitySmoke;
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
		Vec3 right = plylook.crossProduct(Vec3.getTempVec3(0, 1, 0)).normalize();


		if (this.reloadTimer == 0) {
			int count = 3;
			double spacing = 0.6;
			double speed = 0.05;
			double distanceForward = 2;

			for (int i = 0; i < count; i++) {
				double offsetIndex = i - (count - 1) / 2.0;

				// Сдвиг по правой стороне (общий)
				double sideOffsetX = right.x * offsetIndex * spacing;
				double sideOffsetY = right.y * offsetIndex * spacing;
				double sideOffsetZ = right.z * offsetIndex * spacing;

				// ---------- Сущность ----------
				double spawnX = entityplayer.x + plylook.x * distanceForward + sideOffsetX;
				double spawnY = entityplayer.y + entityplayer.getHeadHeight() + plylook.y * distanceForward + sideOffsetY;
				double spawnZ = entityplayer.z + plylook.z * distanceForward + sideOffsetZ;

				Vec3 direction = plylook.normalize();

				double dx = direction.x * speed;
				double dy = direction.y * speed;
				double dz = direction.z * speed;

				EntitySmoke smoke = new EntitySmoke(world, spawnX, spawnY, spawnZ);
				world.entityJoinedWorld(smoke);

				// ---------- Частица (опционально) ----------
				double px = spawnX;
				double py = spawnY;
				double pz = spawnZ;
				double pdx = dx;
				double pdy = dy;
				double pdz = dz;

				world.spawnParticle("bigsmoke", px, py, pz, pdx, pdy, pdz, 2);
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
