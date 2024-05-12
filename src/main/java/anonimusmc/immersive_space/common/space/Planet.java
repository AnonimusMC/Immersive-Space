package anonimusmc.immersive_space.common.space;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import org.joml.Quaternionf;

import java.util.Random;

public class Planet extends CelestialBody {
    private ResourceLocation modelLoc;
    private Vec3 offset;
    private Vec3 scale;
    private float angle = new Random().nextInt(360);
    private float dayAngle = new Random().nextInt(360);
    private Star star;
    private float angularSpeed;
    private float daySpeed;
    private ResourceKey<Level> planetDimension;

    public Planet(ResourceLocation registryName, Star star, Vec3 offset, float angularSpeed, float daySpeed, Vec3 scale, ResourceKey<Level> planetDimension) {
        this(registryName, star, offset, angularSpeed, daySpeed, scale);
        this.planetDimension = planetDimension;
    }

    public Planet(ResourceLocation registryName, Star star, Vec3 offset, float angularSpeed, float daySpeed, Vec3 scale) {
        super(registryName);
        modelLoc = new ResourceLocation(registryName.getNamespace(), "planet/" + registryName.getPath());
        this.offset = offset;
        this.coordinates = star.getCoordinates().add(offset);
        this.star = star;
        this.angularSpeed = angularSpeed;
        this.daySpeed = daySpeed;
        this.scale = scale;
    }

    @Override
    public void tick(TickEvent.LevelTickEvent event) {
        super.tick(event);
        angle += 0.001F + this.angularSpeed;
        dayAngle += 0.1F + this.daySpeed;
        coordinates = star.getCoordinates().add(offset.yRot((float) Math.toRadians(angle)));
    }

    @Override
    public void registryModel(ModelEvent.RegisterAdditional event) {
        event.register(this.modelLoc);
    }

    @Override
    public void render(RenderLevelStageEvent event) {
        FogRenderer.setupNoFog();
        event.getPoseStack().translate(getCoordinates(event.getPartialTick()).x , getCoordinates(event.getPartialTick()).y, getCoordinates(event.getPartialTick()).z);
        event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(dayAngle)));
        event.getPoseStack().translate(- scale.x / 2, - scale.y / 2, - scale.z / 2);
        event.getPoseStack().scale((float) scale.x, (float) scale.y, (float) scale.z);
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);

        MultiBufferSource pBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = pBuffer
                .getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));
        for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, null)) {
            vertexConsumer.putBulkData(event.getPoseStack().last(), quad, 1, 1, 1, LevelRenderer.getLightColor(Minecraft.getInstance().level, new BlockPos((int) getCoordinates(event.getPartialTick()).x, (int) getCoordinates(event.getPartialTick()).y, (int) getCoordinates(event.getPartialTick()).z)), OverlayTexture.NO_OVERLAY);
        }
    }
}
