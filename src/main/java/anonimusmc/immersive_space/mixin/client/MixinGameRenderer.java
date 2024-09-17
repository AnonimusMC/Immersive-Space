package anonimusmc.immersive_space.mixin.client;

import anonimusmc.immersive_space.ImmersiveSpace;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "getDepthFar", at = @At(value = "HEAD"), cancellable = true)
    public void onGetDepthFar(CallbackInfoReturnable<Float> ci){
        if(Minecraft.getInstance().level.dimension() == ImmersiveSpace.SPACE)
            ci.setReturnValue(Float.POSITIVE_INFINITY);
    }
}
