package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.lax1dude.eaglercraft.TextureLocation;
import net.lax1dude.eaglercraft.adapter.Tessellator;
import net.lax1dude.eaglercraft.DefaultSkinRenderer;
import net.lax1dude.eaglercraft.EaglerAdapter;
import net.lax1dude.eaglercraft.EaglercraftRandom;


public abstract class RenderLiving extends Render {
	protected ModelBase mainModel;

	/** The model to be used during the render passes. */
	protected ModelBase renderPassModel;

	public RenderLiving(ModelBase par1ModelBase, float par2) {
		this.mainModel = par1ModelBase;
		this.shadowSize = par2;
	}

	/**
	 * Sets the model to be used in the current render pass (the first render pass
	 * is done after the primary model is rendered) Args: model
	 */
	public void setRenderPassModel(ModelBase par1ModelBase) {
		this.renderPassModel = par1ModelBase;
	}

	/**
	 * Returns a rotation angle that is inbetween two other rotation angles. par1
	 * and par2 are the angles between which to interpolate, par3 is probably a
	 * float between 0.0 and 1.0 that tells us where "between" the two angles we
	 * are. Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
	 */
	private float interpolateRotation(float par1, float par2, float par3) {
		float var4;

		for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F) {
			;
		}

		while (var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		return par1 + par3 * var4;
	}
	
	private static final TextureLocation glint = new TextureLocation("%blur%/misc/glint.png");

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		EaglerAdapter.glPushMatrix();
		EaglerAdapter.glDisable(EaglerAdapter.GL_CULL_FACE);
		this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);

		if (this.renderPassModel != null) {
			this.renderPassModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = par1EntityLiving.isRiding();

		if (this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		this.mainModel.isChild = par1EntityLiving.isChild();

		if (this.renderPassModel != null) {
			this.renderPassModel.isChild = this.mainModel.isChild;
		}

		try {
			float var10 = this.interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
			float var11 = this.interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
			float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
			this.renderLivingAt(par1EntityLiving, par2, par4, par6);
			float var13 = this.handleRotationFloat(par1EntityLiving, par9);
			this.rotateCorpse(par1EntityLiving, var13, var10, par9);
			float var14 = 0.0625F;
			EaglerAdapter.glEnable(EaglerAdapter.GL_RESCALE_NORMAL);
			EaglerAdapter.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(par1EntityLiving, par9);
			EaglerAdapter.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
			float var15 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
			float var16 = par1EntityLiving.limbSwing - par1EntityLiving.limbYaw * (1.0F - par9);

			if (par1EntityLiving.isChild()) {
				var16 *= 3.0F;
			}

			if (var15 > 1.0F) {
				var15 = 1.0F;
			}

			EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
			this.mainModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
			this.renderModel(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
			int var18;
			float var19;
			float var20;
			float var22;

			for (int var17 = 0; var17 < 4; ++var17) {
				var18 = this.shouldRenderPass(par1EntityLiving, var17, par9);

				if (var18 > 0) {
					this.renderPassModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
					this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

					if ((var18 & 240) == 16) {
						this.func_82408_c(par1EntityLiving, var17, par9);
						this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
					}

					if ((var18 & 15) == 15) {
						var19 = (float) par1EntityLiving.ticksExisted + par9;
						glint.bindTexture();
						EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
						var20 = 0.5F;
						EaglerAdapter.glColor4f(var20, var20, var20, 1.0F);
						EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);
						EaglerAdapter.glDepthMask(false);

						for (int var21 = 0; var21 < 2; ++var21) {
							EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
							var22 = 0.76F;
							EaglerAdapter.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
							EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_COLOR, EaglerAdapter.GL_ONE);
							EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
							EaglerAdapter.glLoadIdentity();
							float var23 = var19 * (0.001F + (float) var21 * 0.003F) * 20.0F;
							float var24 = 0.33333334F;
							EaglerAdapter.glScalef(var24, var24, var24);
							EaglerAdapter.glRotatef(30.0F - (float) var21 * 60.0F, 0.0F, 0.0F, 1.0F);
							EaglerAdapter.glTranslatef(0.0F, var23, 0.0F);
							EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}

						EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						EaglerAdapter.glMatrixMode(EaglerAdapter.GL_TEXTURE);
						EaglerAdapter.glDepthMask(true);
						EaglerAdapter.glLoadIdentity();
						EaglerAdapter.glMatrixMode(EaglerAdapter.GL_MODELVIEW);
						EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
						EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
						EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
					}

					EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
				}
			}

			EaglerAdapter.glDepthMask(true);
			this.renderEquippedItems(par1EntityLiving, par9);
			float var26 = par1EntityLiving.getBrightness(par9);
			var18 = this.getColorMultiplier(par1EntityLiving, var26, par9);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

			if ((var18 >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
				EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
				EaglerAdapter.glDisable(EaglerAdapter.GL_ALPHA_TEST);
				EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_EQUAL);

				if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
					EaglerAdapter.glColor4f(var26, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

					for (int var27 = 0; var27 < 4; ++var27) {
						if (this.inheritRenderPass(par1EntityLiving, var27, par9) >= 0) {
							EaglerAdapter.glColor4f(var26, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				if ((var18 >> 24 & 255) > 0) {
					var19 = (float) (var18 >> 16 & 255) / 255.0F;
					var20 = (float) (var18 >> 8 & 255) / 255.0F;
					float var28 = (float) (var18 & 255) / 255.0F;
					var22 = (float) (var18 >> 24 & 255) / 255.0F;
					EaglerAdapter.glColor4f(var19, var20, var28, var22);
					this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

					for (int var29 = 0; var29 < 4; ++var29) {
						if (this.inheritRenderPass(par1EntityLiving, var29, par9) >= 0) {
							EaglerAdapter.glColor4f(var19, var20, var28, var22);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				EaglerAdapter.glDepthFunc(EaglerAdapter.GL_LEQUAL);
				EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
				EaglerAdapter.glEnable(EaglerAdapter.GL_ALPHA_TEST);
				EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			}

			EaglerAdapter.glDisable(EaglerAdapter.GL_RESCALE_NORMAL);
		} catch (Exception var25) {
			var25.printStackTrace();
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		EaglerAdapter.glEnable(EaglerAdapter.GL_CULL_FACE);
		EaglerAdapter.glPopMatrix();
		this.passSpecialRender(par1EntityLiving, par2, par4, par6);
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.bindTexture(par1EntityLiving);

		if (!par1EntityLiving.isInvisible()) {
			this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
		} else if (!par1EntityLiving.func_98034_c(Minecraft.getMinecraft().thePlayer)) {
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
			EaglerAdapter.glDepthMask(false);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.003921569F);
			this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glAlphaFunc(EaglerAdapter.GL_GREATER, 0.1F);
			EaglerAdapter.glPopMatrix();
			EaglerAdapter.glDepthMask(true);
		} else {
			this.mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLiving);
		}
	}

	protected abstract void bindTexture(EntityLiving par1EntityLiving);

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		EaglerAdapter.glTranslatef((float) par2, (float) par4, (float) par6);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		EaglerAdapter.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);

		if (par1EntityLiving.deathTime > 0) {
			float var5 = ((float) par1EntityLiving.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
			var5 = MathHelper.sqrt_float(var5);

			if (var5 > 1.0F) {
				var5 = 1.0F;
			}

			EaglerAdapter.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLiving), 0.0F, 0.0F, 1.0F);
		}
	}

	protected float renderSwingProgress(EntityLiving par1EntityLiving, float par2) {
		return par1EntityLiving.getSwingProgress(par2);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return (float) par1EntityLiving.ticksExisted + par2;
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
	}

	/**
	 * renders arrows the Entity has been attacked with, attached to it
	 */
	protected void renderArrowsStuckInEntity(EntityLiving par1EntityLiving, float par2) {
		int var3 = par1EntityLiving.getArrowCountInEntity();

		if (var3 > 0) {
			EntityArrow var4 = new EntityArrow(par1EntityLiving.worldObj, par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
			EaglercraftRandom var5 = new EaglercraftRandom((long) par1EntityLiving.entityId);
			RenderHelper.disableStandardItemLighting();

			for (int var6 = 0; var6 < var3; ++var6) {
				EaglerAdapter.glPushMatrix();
				ModelRenderer var7 = this.mainModel.getRandomModelBox(var5);
				ModelBox var8 = (ModelBox) var7.cubeList.get(var5.nextInt(var7.cubeList.size()));
				var7.postRender(0.0625F);
				float var9 = var5.nextFloat();
				float var10 = var5.nextFloat();
				float var11 = var5.nextFloat();
				float var12 = (var8.posX1 + (var8.posX2 - var8.posX1) * var9) / 16.0F;
				float var13 = (var8.posY1 + (var8.posY2 - var8.posY1) * var10) / 16.0F;
				float var14 = (var8.posZ1 + (var8.posZ2 - var8.posZ1) * var11) / 16.0F;
				EaglerAdapter.glTranslatef(var12, var13, var14);
				var9 = var9 * 2.0F - 1.0F;
				var10 = var10 * 2.0F - 1.0F;
				var11 = var11 * 2.0F - 1.0F;
				var9 *= -1.0F;
				var10 *= -1.0F;
				var11 *= -1.0F;
				float var15 = MathHelper.sqrt_float(var9 * var9 + var11 * var11);
				var4.prevRotationYaw = var4.rotationYaw = (float) (Math.atan2((double) var9, (double) var11) * 180.0D / Math.PI);
				var4.prevRotationPitch = var4.rotationPitch = (float) (Math.atan2((double) var10, (double) var15) * 180.0D / Math.PI);
				double var16 = 0.0D;
				double var18 = 0.0D;
				double var20 = 0.0D;
				float var22 = 0.0F;
				this.renderManager.renderEntityWithPosYaw(var4, var16, var18, var20, var22, par2);
				EaglerAdapter.glPopMatrix();
			}

			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.shouldRenderPass(par1EntityLiving, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return -1;
	}

	protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
	}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return 90.0F;
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness,
	 * partialTickTime
	 */
	protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3) {
		return 0;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the
	 * model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
	}

	/**
	 * Passes the specialRender and renders it
	 */
	protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		if (Minecraft.isGuiEnabled() && par1EntityLiving != this.renderManager.livingPlayer && !par1EntityLiving.func_98034_c(Minecraft.getMinecraft().thePlayer)
				&& (par1EntityLiving.func_94059_bO() || par1EntityLiving.func_94056_bM() && par1EntityLiving == this.renderManager.field_96451_i)) {
			float var8 = 1.6F;
			float var9 = 0.016666668F * var8;
			double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);
			float var12 = par1EntityLiving.isSneaking() ? 32.0F : 64.0F;

			if (var10 < (double) (var12 * var12)) {
				String var13 = par1EntityLiving.getTranslatedEntityName();

				if (par1EntityLiving.isSneaking()) {
					FontRenderer var14 = this.getFontRendererFromRenderManager();
					EaglerAdapter.glPushMatrix();
					EaglerAdapter.glTranslatef((float) par2 + 0.0F, (float) par4 + par1EntityLiving.height + 0.5F, (float) par6);
					EaglerAdapter.glNormal3f(0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
					EaglerAdapter.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
					EaglerAdapter.glScalef(-var9, -var9, var9);
					EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glTranslatef(0.0F, 0.25F / var9, 0.0F);
					EaglerAdapter.glDepthMask(false);
					EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
					Tessellator var15 = Tessellator.instance;
					EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
					var15.startDrawingQuads();
					int var16 = var14.getStringWidth(var13) / 2;
					var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
					var15.addVertex((double) (-var16 - 1), -1.0D, 0.0D);
					var15.addVertex((double) (-var16 - 1), 8.0D, 0.0D);
					var15.addVertex((double) (var16 + 1), 8.0D, 0.0D);
					var15.addVertex((double) (var16 + 1), -1.0D, 0.0D);
					var15.draw();
					EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
					EaglerAdapter.glDepthMask(true);
					var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0, 553648127);
					EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
					EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
					EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					EaglerAdapter.glPopMatrix();
				} else {
					this.func_96449_a(par1EntityLiving, par2, par4, par6, var13, var9, var10);
				}
			}
		}
	}

	protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		if (par1EntityLiving.isPlayerSleeping()) {
			this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4 - 1.5D, par6, 64);
		} else {
			this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4, par6, 64);
		}
	}

	/**
	 * Draws the debug or playername text above a living
	 */
	protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9) {
		double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);

		if (var10 <= (double) (par9 * par9)) {
			FontRenderer var12 = this.getFontRendererFromRenderManager();
			float var13 = 1.6F;
			float var14 = 0.016666668F * var13;
			EaglerAdapter.glPushMatrix();
			EaglerAdapter.glTranslatef((float) par3 + 0.0F, (float) par5 + par1EntityLiving.height + 0.5F, (float) par7);
			EaglerAdapter.glNormal3f(0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			EaglerAdapter.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			EaglerAdapter.glScalef(-var14, -var14, var14);
			EaglerAdapter.glDisable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glDepthMask(false);
			EaglerAdapter.glDisable(EaglerAdapter.GL_DEPTH_TEST);
			EaglerAdapter.glEnable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glBlendFunc(EaglerAdapter.GL_SRC_ALPHA, EaglerAdapter.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator var15 = Tessellator.instance;
			byte var16 = 0;

			if (par2Str.equals("deadmau5")) {
				var16 = -10;
			}
			
			if(par1EntityLiving instanceof EntityOtherPlayerMP) {
				if(DefaultSkinRenderer.getPlayerRenderer((EntityOtherPlayerMP)par1EntityLiving) == 19) {
					var16 = -32;
				}
			}

			EaglerAdapter.glDisable(EaglerAdapter.GL_TEXTURE_2D);
			var15.startDrawingQuads();
			int var17 = var12.getStringWidth(par2Str) / 2;
			var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			var15.addVertex((double) (-var17 - 1), (double) (-1 + var16), 0.0D);
			var15.addVertex((double) (-var17 - 1), (double) (8 + var16), 0.0D);
			var15.addVertex((double) (var17 + 1), (double) (8 + var16), 0.0D);
			var15.addVertex((double) (var17 + 1), (double) (-1 + var16), 0.0D);
			var15.draw();
			EaglerAdapter.glEnable(EaglerAdapter.GL_TEXTURE_2D);
			var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, 553648127);
			EaglerAdapter.glEnable(EaglerAdapter.GL_DEPTH_TEST);
			EaglerAdapter.glDepthMask(true);
			var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, -1);
			EaglerAdapter.glEnable(EaglerAdapter.GL_LIGHTING);
			EaglerAdapter.glDisable(EaglerAdapter.GL_BLEND);
			EaglerAdapter.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			EaglerAdapter.glPopMatrix();
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker function
	 * which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void
	 * doRender(T entity, double d, double d1, double d2, float f, float f1). But
	 * JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderLiving((EntityLiving) par1Entity, par2, par4, par6, par8, par9);
	}
}
