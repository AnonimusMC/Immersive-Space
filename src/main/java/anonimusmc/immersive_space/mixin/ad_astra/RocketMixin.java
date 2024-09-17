package anonimusmc.immersive_space.mixin.ad_astra;

import anonimusmc.immersive_space.ImmersiveSpace;
import anonimusmc.immersive_space.common.space.CelestialBody;
import earth.terrarium.adastra.common.config.AdAstraConfig;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import earth.terrarium.adastra.common.tags.ModFluidTags;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Rocket.class)
public abstract class RocketMixin extends Entity {

    private float zAngle;

    @Shadow(remap = false)
    public abstract void explode();

    @Shadow(remap = false)
    private float angle;

    @Shadow(remap = false)
    @Final
    private SimpleFluidContainer fluidContainer;

    @Shadow(remap = false)
    public abstract FluidHolder fluid();

    public RocketMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "initiateLaunchSequence", remap = false, at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/entities/vehicles/Rocket;consumeFuel(Z)Z"), cancellable = true)
    public void initiateLaunch(CallbackInfo ci) {
        ci.cancel();
        this.fluidContainer.getFirstFluid().setAmount(this.fluidContainer.getFirstFluid().getFluidAmount() - (this.fluidContainer.getFirstFluid().is(ModFluidTags.EFFICIENT_FUEL) ? 10 : 30));
    }

    @Inject(method = "hasEnoughFuel", remap = false, at = @At(value = "HEAD"), cancellable = true)
    public void enoughFel(CallbackInfoReturnable ci) {
        ci.setReturnValue(this.fluidContainer.getFirstFluid().getFluidAmount() > (this.fluidContainer.getFirstFluid().is(ModFluidTags.EFFICIENT_FUEL) ? 10 : 30));
    }

    @Inject(method = "flightTick", remap = false, at = @At(value = "HEAD"), cancellable = true)
    public void flightTick(CallbackInfo ci) {
        if (this.level().dimension() == ImmersiveSpace.SPACE) {
            ci.cancel();
            if (getControllingPassenger() instanceof Player && !fluid().isEmpty()) {
                float xxa = -((Vehicle) (Object) this).xxa();
                if (xxa != 0.0F) {
                    this.angle += xxa * 0.1F;
                } else {
                    this.angle *= 0.9F;
                }
                float zza = -((Vehicle) (Object) this).zza();
                if (zza != 0.0F) {
                    this.zAngle -= zza * 0.1F;
                } else {
                    this.zAngle *= 0.9F;
                }
                setXRot(getXRot() + zAngle);
                setYRot(getYRot() + angle);
                this.setDeltaMovement(getViewVector(0).multiply(new Vec3(10,10,10)));

                if (tickCount % 40 == 0 && !((Player) getControllingPassenger()).isCreative())
                    this.fluidContainer.getFirstFluid().setAmount(this.fluidContainer.getFirstFluid().getFluidAmount() - (this.fluidContainer.getFirstFluid().is(ModFluidTags.EFFICIENT_FUEL) ? 1 : 3));
            }
        } else if (!this.level().isClientSide() && this.getY() >= (double) AdAstraConfig.atmosphereLeave) {
            ci.cancel();
            LivingEntity var4 = this.getControllingPassenger();
            if (var4 instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) var4;
                player = (ServerPlayer) moveToSpace(player);
                this.ejectPassengers();
                Rocket rocket = moveToSpace((Rocket) (Object) this);
                rocket.fluidContainer().getFirstFluid().setAmount(this.fluidContainer.getFirstFluid().getFluidAmount());
                player.startRiding(rocket);
            } else {
                this.explode();
            }
        }
    }

    public <T extends Entity> T moveToSpace(T entity) {
        return (T) entity.changeDimension(((ServerLevel) entity.level()).getServer().getLevel(ImmersiveSpace.SPACE), new ITeleporter() {


            @Override
            public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
                return false;
            }

            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                System.out.println("old dim: " + entity.level().dimension().location());
                Vec3 position = CelestialBody.getExitableBodyFromDimension(entity.level().dimension()).getExitPosition();
                Entity repositionedEntity = repositionEntity.apply(false);
                repositionedEntity.teleportTo(position.x, position.y, position.z);
                repositionedEntity.setNoGravity(false);
                return repositionedEntity;
            }
        });
    }
}
