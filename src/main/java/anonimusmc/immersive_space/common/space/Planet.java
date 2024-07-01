package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.ImmersiveSpace;
import anonimusmc.immersive_space.client.ImmersiveSpaceRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.adastra.common.config.AdAstraConfig;
import earth.terrarium.adastra.common.handlers.LaunchingDimensionHandler;
import earth.terrarium.adastra.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import org.joml.Quaternionf;

import java.util.Random;
import java.util.function.Supplier;

public class Planet extends CelestialBody implements ExitableBody {
    private ResourceLocation modelLoc;
    private Vec3 offset;
    private Vec3 scale;
    private float angle = new Random().nextInt(360);
    private float dayAngle = new Random().nextInt(360);
    private Star star;
    private float angularSpeed;
    private float daySpeed;
    private AABB boundingBox;
    private Supplier<earth.terrarium.adastra.api.planets.Planet> planet;

    public Planet(ResourceLocation registryName, Star star, Vec3 offset, float angularSpeed, float daySpeed, Vec3 scale, Supplier<earth.terrarium.adastra.api.planets.Planet> planet) {
        this(registryName, star, offset, angularSpeed, daySpeed, scale);
        this.planet = planet;
    }

    private ResourceKey<Level> dim() {
        return planet.get().orbitIfPresent();
    }

    public Planet(ResourceLocation registryName, Star star, Vec3 offset, float angularSpeed, float daySpeed, Vec3 scale) {
        super(registryName);
        modelLoc = new ResourceLocation(registryName.getNamespace(), "planet/" + registryName.getPath());
        this.offset = offset.multiply(ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE);
        this.coordinates = star.getCoordinates().add(this.offset);
        this.star = star;
        this.angularSpeed = (float) (angularSpeed/Math.pow(ImmersiveSpace.SPACE_SCALE, 2));
        this.daySpeed = daySpeed/ImmersiveSpace.SPACE_SCALE;
        this.scale = scale.multiply(ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE);
        this.boundingBox = new AABB(-this.scale.x / 2, -this.scale.y / 2, -this.scale.z / 2, this.scale.x / 2, this.scale.y / 2, this.scale.z / 2);
    }

    @Override
    public void tick(TickEvent.LevelTickEvent event) {
        super.tick(event);
        angle += 0.001F + this.angularSpeed / 2;
        dayAngle += 0.001F + this.daySpeed;
        coordinatesO = coordinates;
        coordinates = star.getCoordinates().add(offset.yRot((float) Math.toRadians(angle)));
        if (!event.level.isClientSide && planet != null) {
            event.level.getEntities((Entity) null, boundingBox.move(coordinates), entity -> true).forEach(entity -> {
                if (!entity.isPassenger()) {
                    if (entity.isVehicle())
                        moveToPlanet((ServerPlayer) entity.getFirstPassenger());
                }
            });
        }
    }

    public void moveToPlanet(ServerPlayer entity) {
        ServerLevel level = ((ServerLevel) entity.level()).getServer().getLevel(dim());
        ModUtils.land(entity, level, LaunchingDimensionHandler.getSpawningLocation(entity, level, planet.get()).orElse(GlobalPos.of(dim(), new BlockPos(0, AdAstraConfig.atmosphereLeave, 0))).pos().getCenter());
    }

    @Override
    public void registryModel(ModelEvent.RegisterAdditional event) {
        event.register(this.modelLoc);
    }

    @Override
    public void render(RenderLevelStageEvent event) {
        MultiBufferSource pBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        event.getPoseStack().translate(getCoordinates(event.getPartialTick()).x, getCoordinates(event.getPartialTick()).y, getCoordinates(event.getPartialTick()).z);
        if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            LevelRenderer.renderLineBox(event.getPoseStack(), pBuffer.getBuffer(RenderType.LINES), boundingBox, 1, 1, 1, 1);
        }
        event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(dayAngle)));
        event.getPoseStack().translate(-scale.x / 2, -scale.y / 2, -scale.z / 2);
        event.getPoseStack().scale((float) scale.x, (float) scale.y, (float) scale.z);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);

        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource()
                .getBuffer(ImmersiveSpaceRenderTypes.celestialBodies(InventoryMenu.BLOCK_ATLAS));
        for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, null)) {
            vertexConsumer.putBulkData(event.getPoseStack().last(), quad, 1, 1, 1, LevelRenderer.getLightColor(Minecraft.getInstance().level, new BlockPos((int) getCoordinates(event.getPartialTick()).x, (int) getCoordinates(event.getPartialTick()).y, (int) getCoordinates(event.getPartialTick()).z)), OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    public Vec3 getExitPosition() {
        return getCoordinates().add(0, scale.y + 10, 0);
    }

    @Override
    public boolean isFromDimension(ResourceKey<Level> dim) {
        return planet != null && dim == planet.get().dimension();
    }
}
