package anonimusmc.immersive_space.mixin.ad_astra;

import anonimusmc.immersive_space.ImmersiveSpace;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.adastra.client.renderers.entities.vehicles.RocketRenderer;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RocketRenderer.class)
public abstract class RocketRendererMixin {

    @Shadow @Final protected EntityModel<Rocket> model;

    @Shadow public abstract ResourceLocation getTextureLocation(Rocket entity);

    @Inject(method = "render(Learth/terrarium/adastra/common/entities/vehicles/Rocket;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", remap = false, at = @At(value = "INVOKE",
            target = "Lcom/teamresourceful/resourcefullib/client/CloseablePoseStack;translate(FFF)V", shift = At.Shift.AFTER
    ), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderBegin(Rocket entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci, CloseablePoseStack pose) {
        if (Minecraft.getInstance().level.dimension() == ImmersiveSpace.SPACE) {
            ci.cancel();
            pose.mulPose(new Quaternionf().rotationY((float) Math.toRadians(180 - Mth.lerp(partialTick, entity.yRotO, entity.getYRot()))));
            pose.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90 - Mth.lerp(partialTick, entity.xRotO, entity.getXRot()))));
//            pose.scale(-1.0F, -1.0F, 1.0F);
            this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            VertexConsumer consumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
            this.model.renderToBuffer(pose, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pose.close();
        }
    }
}
