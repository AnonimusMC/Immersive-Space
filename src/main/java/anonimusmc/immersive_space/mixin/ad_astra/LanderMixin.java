package anonimusmc.immersive_space.mixin.ad_astra;

import earth.terrarium.adastra.common.entities.vehicles.Lander;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Lander.class)
public abstract class LanderMixin extends Entity {

    @Shadow private float speed;

    public LanderMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "flightTick", remap = false, at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/entities/vehicles/Lander;setDeltaMovement(DDD)V", ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void flightTick(CallbackInfo ci, Vec3 delta){
        ci.cancel();
        this.setDeltaMovement(delta.x() + getViewVector(0).x * ((Vehicle) (Object) this).zza()*0.05, (double)this.speed, delta.z() + getViewVector(0).z * ((Vehicle) (Object) this).zza()*0.05);
        if (this.isInWater()) {
            this.setDeltaMovement(delta.x(), Math.min(0.06D, delta.y() + 0.15D), delta.z());
            this.speed *= 0.9F;
        }
    }
}
