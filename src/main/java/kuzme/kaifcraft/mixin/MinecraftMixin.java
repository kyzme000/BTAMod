package kuzme.kaifcraft.mixin;

import com.mojang.nbt.tags.ByteTag;
import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.item.ItemBlunt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.MinecraftAccessor;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketCustomPayload;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixin {
	@Inject(method = "runTick", at = @At("TAIL"))
	private void kaifcraft$onClientTick(CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer == null || mc.currentWorld == null) return;

		ItemStack held = mc.thePlayer.getHeldItem();
		boolean holding = Mouse.isButtonDown(1);

		if (held != null && held.getItem() instanceof ItemBlunt) {
			CompoundTag tag = held.getData();
			boolean last = tag.getBoolean("LastHold");
			if (last != holding) {
				tag.putBoolean("LastHold", holding);
				System.out.println("[Kaifcraft][Client] LastHold: " + last + " -> " + holding);
				sendHoldPacket(holding);
			}
		}
	}

	private void sendHoldPacket(boolean holding) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteStream);
			out.writeBoolean(holding);

			System.out.println("[Kaifcraft][Client] Packet sent: holding=" + holding);

			PacketCustomPayload packet = new PacketCustomPayload("KaifBluntHold", byteStream.toByteArray());
			if (Minecraft.getMinecraft().getSendQueue() != null) {
				Minecraft.getMinecraft().getSendQueue().addToSendQueue(packet);
			}
		} catch (IOException e) {
			System.err.println("[Kaifcraft][Client] Ошибка отправки пакета KaifBluntHold: " + e);
		}
	}
}

