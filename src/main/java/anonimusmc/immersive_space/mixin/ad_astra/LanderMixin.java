package anonimusmc.immersive_space.mixin.ad_astra;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Lander.class)
public abstract class LanderMixin extends Entity {

    @Shadow(remap = false) private float speed;
    private boolean landed = false;

    public LanderMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
            method = "flightTick", remap = false, at = @At(value = "INVOKE", remap = true, target = "Learth/terrarium/adastra/common/entities/vehicles/Lander;setDeltaMovement(DDD)V", ordinal = 0))
    public void flightTick(Lander instance, double deltaX, double deltaY, double deltaZ, Operation<Void> original){
        if(instance.onGround() || instance.isInWater())
            landed = true;
        instance.setXRot(0);
        if(!landed){
            original.call(instance, deltaX + getViewVector(0).x * ((Vehicle) (Object) this).zza() * 0.05, deltaY, deltaZ + getViewVector(0).z * ((Vehicle) (Object) this).zza() * 0.05);
        }
    }
}
