package anonimusmc.immersive_space.mixin.client;

import anonimusmc.immersive_space.ImmersiveSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Options.class)
public class MixinOptions {

    @Inject(method = "getEffectiveRenderDistance", at = @At("HEAD"), cancellable = true)
    public void onGetEffectiveRenderDistance(CallbackInfoReturnable<Integer> ci) {
        if (Minecraft.getInstance().level.dimension() == ImmersiveSpace.SPACE)
            ci.setReturnValue(2000);
    }
}
