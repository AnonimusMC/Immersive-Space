package anonimusmc.immersive_space.mixin;

import anonimusmc.immersive_space.ImmersiveSpace;
import earth.terrarium.adastra.common.config.AdAstraConfig;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private Level level;

    @Inject(method = "onBelowWorld", remap = false, at = @At(value = "HEAD"), cancellable = true)
    public void flightTick(CallbackInfo ci){
        if(this.level.dimension() == ImmersiveSpace.SPACE)
            ci.cancel();
    }
}
