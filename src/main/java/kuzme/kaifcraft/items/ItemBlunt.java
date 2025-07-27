package kuzme.kaifcraft.items;


import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import java.util.Random;

public class ItemBlunt extends Item {



	public final Random random = new Random();
	public ItemBlunt(String name, String namespaceId, int id) {
		super(name, namespaceId, id);
	}

	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {

			world.spawnParticle(
				"largesmoke", entityplayer.x, entityplayer.y-0.2, entityplayer.z, 0, 0, 0, 2
			);
			world.playSoundAtEntity(entityplayer, entityplayer, "kaifcraft:cough", 0.45F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F);

		return itemstack;
	}


}
