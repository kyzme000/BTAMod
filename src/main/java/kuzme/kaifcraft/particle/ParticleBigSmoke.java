package kuzme.kaifcraft.particle;

import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.world.World;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

public class ParticleBigSmoke extends Particle {

	public static final IconCoordinate bigSmoke1 = TextureRegistry.getTexture(MOD_ID + ":particle/big_smoke");
	public static final IconCoordinate bigSmoke2 = TextureRegistry.getTexture(MOD_ID + ":particle/big_smoke_2");
	public float originalScale;
	public ParticleBigSmoke(World world, double x, double y, double z, double xa, double ya, double za) {
		super(world, x, y, z, xa, ya, za);
		this.tex = TextureRegistry.getTexture(MOD_ID + ":particle/big_smoke");
		this.rCol = 1;
		this.gCol = 1;
		this.bCol = 1;
		this.size *= 5.0f;
		this.originalScale = this.size;
		this.lifetime = random.nextInt(60) + 80;
		this.age = 1;

		this.xd = xa;
		this.yd = ya;
		this.zd = za;
	}
	@Override
	public void render(Tessellator tessellator, float partialTick, double x, double y, double z, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		float progress = (float) this.age / this.lifetime;
		this.size = this.originalScale * (1.0f - progress * 0.6f);
		super.render(tessellator, partialTick, x, y, z, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
	}

	public void tick() {
		this.tex = (this.age / 50) % 2 == 0 ? bigSmoke1 : bigSmoke2;

		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		}

		this.yd += 0.0001;

		// Легкие хаотичные колебания по X и Z
		this.xd += (random.nextDouble() - 0.5) * 0.002;
		this.zd += (random.nextDouble() - 0.5) * 0.002;

		// Уменьшаем замеёдление по X и Z, чтобы дым дольше летел вперёд
		this.xd *= 0.992;
		this.yd *= 0.98;
		this.zd *= 0.992;
	    this.move(this.xd, this.yd, this.zd);
		if (this.onGround) {
			this.xd *= 0.7;
			this.zd *= 0.7;
		}
	}
}
