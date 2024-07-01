package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.ImmersiveSpace;
import anonimusmc.immersive_space.client.ImmersiveSpaceRenderTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
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

public class Star extends CelestialBody {
    private ResourceLocation modelLoc;
    private Vec3 scale;

    public Star(ResourceLocation registryName, Vec3 offset, Vec3 scale) {
        super(registryName);
        modelLoc = new ResourceLocation(registryName.getNamespace(), "star/" + registryName.getPath());
        this.coordinates = offset.multiply(ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE);
        this.scale = scale.multiply(ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE,ImmersiveSpace.SPACE_SCALE);
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

        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource()
                .getBuffer(ImmersiveSpaceRenderTypes.celestialBodies(InventoryMenu.BLOCK_ATLAS));
        for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, null)) {
            vertexConsumer.putBulkData(event.getPoseStack().last(), quad, 1, 1, 1, LevelRenderer.getLightColor(Minecraft.getInstance().level, new BlockPos((int) getCoordinates(event.getPartialTick()).x, (int) getCoordinates(event.getPartialTick()).y, (int) getCoordinates(event.getPartialTick()).z)), OverlayTexture.NO_OVERLAY);
        }
    }
}
