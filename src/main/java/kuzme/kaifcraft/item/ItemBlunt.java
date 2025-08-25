package kuzme.kaifcraft.item;


import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import kuzme.kaifcraft.entity.EntitySmoke;
import kuzme.kaifcraft.util.KaifNet;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.Random;

public class ItemBlunt extends Item {

	public final Random random = new Random();


	public ItemBlunt(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	private static int getCooldown(ItemStack stack) {
		CompoundTag tag = stack.getData();
		return tag.getInteger("ReloadTimer");
	}

	private static void setCooldown(ItemStack stack, int ticks) {
		CompoundTag tag = stack.getData();
		tag.put("ReloadTimer", new IntTag(ticks));
	}


	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {

		Vec3 plylook = entityplayer.getLookAngle();
		Vec3 right = plylook.crossProduct(Vec3.getTempVec3(0, 1, 0)).normalize();

		int reload = getCooldown(itemstack);
		if (reload != 0) return itemstack;

		//  сервер
		if (EnvironmentHelper.isServerEnvironment()) {

			double distanceForward = 2;
			double entitySpawnX = entityplayer.x + plylook.x * distanceForward;
			double entitySpawnY = entityplayer.y + plylook.y * distanceForward;
			double entitySpawnZ = entityplayer.z + plylook.z * distanceForward;

			EntitySmoke smoke = new EntitySmoke(world, entitySpawnX, entitySpawnY, entitySpawnZ);
			world.entityJoinedWorld(smoke);

			int count = 3;
			double spacing = 0.6;
			double speed = 0.05;

			for (int i = 0; i < count; i++) {
				double offsetIndex = i - (count - 1) / 2.0;

				double offsetX = right.x * offsetIndex * spacing;
				double offsetY = right.y * offsetIndex * spacing;
				double offsetZ = right.z * offsetIndex * spacing;

				double spawnX = entityplayer.x + plylook.x * 0.5 + offsetX;
				double spawnY = entityplayer.y + entityplayer.getHeadHeight() + plylook.y * 0.5 + offsetY;
				double spawnZ = entityplayer.z + plylook.z * 0.5 + offsetZ;

				Vec3 direction = entityplayer.getLookAngle().normalize();
				double dx = direction.x * speed;
				double dy = direction.y * speed;
				double dz = direction.z * speed;

				KaifNet.sendParticleToNearby(world, spawnX, spawnY, spawnZ, "bigsmoke", dx, dy, dz, 0, 20);
			}

			KaifNet.sendSoundToNearby(world, entityplayer.x, entityplayer.y, entityplayer.z,"kaifcraft:cough",
				0.45F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F);

				setCooldown(itemstack, 100);
		}

		// клиент
		if (EnvironmentHelper.isSinglePlayer()) {

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

			world.playSoundAtEntity(entityplayer, entityplayer, "kaifcraft:cough",
				0.45F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F);
		}

		return itemstack;
	}


	@Override
	public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
			int cooldown = getCooldown(itemstack);
			if (cooldown > 0) {
				setCooldown(itemstack, cooldown - 1);
			}
	}


}
