package anonimusmc.immersive_space.mixin.client;

import anonimusmc.immersive_space.ImmersiveSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "getRenderDistance", at = @At(value = "HEAD"), cancellable = true)
    public void onGetRenderDistance(CallbackInfoReturnable<Float> ci){
        if(Minecraft.getInstance().level.dimension() == ImmersiveSpace.SPACE)
            ci.setReturnValue(1000F);
    }
}
