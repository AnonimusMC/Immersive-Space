package anonimusmc.immersive_space.common.space;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;

import java.util.Random;

public class Satellite extends CelestialBody{

    private Planet planet;
    private ResourceLocation modelLoc;
    private Vec3 offset;
    private Vec3 scale;
    private float angle = new Random().nextInt(360);
    private float angularSpeed;

    public Satellite(ResourceLocation registryName, Planet planet, Vec3 offset, float angularSpeed, Vec3 scale) {
        super(registryName);
        this.planet = planet;
        modelLoc = new ResourceLocation(registryName.getNamespace(), "satellite/" + registryName.getPath());
        this.offset = offset;
        this.coordinates = planet.getCoordinates().add(offset);
        this.angularSpeed = angularSpeed;
        this.scale = scale;
    }
    @Override
    public void tick(TickEvent.LevelTickEvent event) {
        super.tick(event);
        angle += 0.01F + this.angularSpeed;
        coordinates = planet.getCoordinates().add(offset.yRot((float) Math.toRadians(angle)));
    }

    @Override
    public void registryModel(ModelEvent.RegisterAdditional event) {
        event.register(this.modelLoc);
    }

    @Override
    public void render(RenderLevelStageEvent event) {
        event.getPoseStack().translate(getCoordinates(event.getPartialTick()).x-scale.x/2, getCoordinates(event.getPartialTick()).y-scale.y/2, getCoordinates(event.getPartialTick()).z-scale.z/2);
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
