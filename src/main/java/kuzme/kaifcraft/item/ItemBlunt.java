package kuzme.kaifcraft.item;


import com.mojang.nbt.tags.ByteTag;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.IntTag;
import kuzme.kaifcraft.entity.EntitySmoke;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import java.util.Random;

public class ItemBlunt extends Item {

	private final Random random = new Random();

	public ItemBlunt(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!(entity instanceof Player) || world.isClientSide || !selected) return;

		Player player = (Player) entity;

		// DEBUG
		player.sendMessage("[Debug] selected: " + selected);
		player.sendMessage("[Debug] ClientHolding: " + getClientHolding(stack));

		// Кулдаун
		int cd = getCooldown(stack);
		if (cd > 0) {
			setCooldown(stack, cd - 1);
		}

		// Струя
		if (getFlowTimer(stack) > 0) {
			handleSmokeFlow(stack, world, player);
			return;
		}

		boolean isHolding = getClientHolding(stack);

		// Удержание
		if (isHolding) {
			int time = getUseTime(stack) + 1;
			setUseTime(stack, time);

			if (!getWasHolding(stack)) {
				setWasHolding(stack, true);
				setPlayedStartSound(stack, false);
				player.sendMessage("начал удерживать ПКМ");
			}

			if (!getPlayedStartSound(stack)) {
				setPlayedStartSound(stack, true);
				world.playSoundAtEntity(player, player, "kaifcraft:puff", 0.4F, 1.0F);
				player.sendMessage("звук затяжки");
			}

			player.sendMessage("время удержания: " + time);

			if (time > 100) {
				player.sendMessage("передозировка");
				triggerSingleUse(stack, world, player);
				setCooldown(stack, 40);
				resetUseState(stack);
			}

			return;
		}

		// Отпускание
		if (getWasHolding(stack)) {
			int held = getUseTime(stack);
			player.sendMessage("ПКМ отпущен после " + held + " тиков");

			if (held >= 5) {
				setFlowTimer(stack, 30);
				setFlowTicks(stack, 0);
				world.playSoundAtEntity(player, player, "kaifcraft:blow", 1.0F, 1.0F);
				setCooldown(stack, 20);
				player.sendMessage("запуск струи");
			} else {
				triggerSingleUse(stack, world, player);
				setCooldown(stack, 100);
				player.sendMessage("быстрое использование");
			}

			resetUseState(stack);
		}
	}

	private void handleSmokeFlow(ItemStack stack, World world, Player player) {
		int flow = getFlowTimer(stack);
		if (flow > 0) {
			int prog = getFlowTicks(stack);
			setFlowTicks(stack, prog + 1);
			setFlowTimer(stack, flow - 1);

			Vec3 look = player.getLookAngle().normalize();
			double speed = 0.15;

			world.spawnParticle(
				"bigsmoke",
				player.x + look.x * 0.5,
				player.y + player.getHeadHeight() - 0.2,
				player.z + look.z * 0.5,
				look.x * speed,
				look.y * speed,
				look.z * speed,
				5
			);
			player.sendMessage("струя тикает: " + prog);
		}
	}

	// — NBT —
	private static CompoundTag getTag(ItemStack stack) {
		return stack.getData();
	}

	private static boolean getClientHolding(ItemStack stack) {
		return getTag(stack).getBoolean("ClientHolding");
	}

	private void triggerSingleUse(ItemStack stack, World world, Player player) {
		if (world.isClientSide) return; // Спавн только на сервере

		// Получаем вектор взгляда игрока
		Vec3 look = player.getLookAngle().normalize();

		// Смещение спавна впереди игрока (0.5 блока)
		double spawnX = player.x + look.x * 0.5;
		double spawnY = player.y + player.getHeadHeight() - 0.2; // чуть ниже глаз
		double spawnZ = player.z + look.z * 0.5;

		// Создаём и спаунем дымовую сущность
		EntitySmoke smoke = new EntitySmoke(world, spawnX, spawnY, spawnZ);

		world.entityJoinedWorld(smoke);

		// Проигрываем звук у игрока
		world.playSoundAtEntity(player, player, "kaifcraft:puff", 1.0F, 1.0F);

		player.sendMessage("SMOKENTITY");
	}

	private static int getCooldown(ItemStack stack) { return getTag(stack).getInteger("ReloadTimer"); }
	private static void setCooldown(ItemStack stack, int t) { getTag(stack).put("ReloadTimer", new IntTag(t)); }
	private static int getUseTime(ItemStack stack) { return getTag(stack).getInteger("UseTime"); }
	private static void setUseTime(ItemStack stack, int t) { getTag(stack).put("UseTime", new IntTag(t)); }
	private static int getFlowTimer(ItemStack stack) { return getTag(stack).getInteger("FlowTimer"); }
	private static void setFlowTimer(ItemStack stack, int t) { getTag(stack).put("FlowTimer", new IntTag(t)); }
	private static int getFlowTicks(ItemStack stack) { return getTag(stack).getInteger("FlowTicks"); }
	private static void setFlowTicks(ItemStack stack, int t) { getTag(stack).put("FlowTicks", new IntTag(t)); }
	private static boolean getWasHolding(ItemStack stack) { return getTag(stack).getBoolean("WasHolding"); }
	private static void setWasHolding(ItemStack stack, boolean v) { getTag(stack).put("WasHolding", new ByteTag((byte)(v ? 1 : 0))); }
	private static boolean getPlayedStartSound(ItemStack stack) { return getTag(stack).getBoolean("DidStartSound"); }
	private static void setPlayedStartSound(ItemStack stack, boolean v) { getTag(stack).put("DidStartSound", new ByteTag((byte)(v ? 1 : 0))); }

	private void resetUseState(ItemStack stack) {
		setUseTime(stack, 0);
		setWasHolding(stack, false);
		setPlayedStartSound(stack, false);
	}


}
