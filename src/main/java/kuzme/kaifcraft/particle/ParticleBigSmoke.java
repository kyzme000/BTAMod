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
		this.size *= 6.0f;
		this.originalScale = this.size;
		this.lifetime = random.nextInt(40) + 80;
		this.age = 1;
		this.yd = -Math.abs(this.yd);
	}
	@Override
	public void render(Tessellator tessellator, float partialTick, double x, double y, double z, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
		this.size = this.originalScale - this.originalScale * ((float) this.age / this.lifetime);
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

	    this.move(this.xd, this.yd, this.zd);
	    this.xd *= 0.97;
		this.yd *= 0.65;
	    this.zd *= 0.97;
		if (this.onGround) {
			this.xd *= 0.7;
			this.zd *= 0.7;
		}
	}
}
