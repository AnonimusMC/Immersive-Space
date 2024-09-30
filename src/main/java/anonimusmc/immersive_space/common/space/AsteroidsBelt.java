package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.ImmersiveSpace;
import anonimusmc.immersive_space.client.ImmersiveSpaceRenderTypes;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import org.joml.Quaternionf;

import java.util.Random;

public class AsteroidsBelt extends CelestialBody {

    private final int asteroids;
    private final boolean applyBeltWidthScale;
    private float angle = new Random().nextInt(360);
    private float angle0 = angle;
    private float angularSpeed;
    private float radius;
    private ResourceLocation modelLoc1;
    private ResourceLocation modelLoc2;


    public AsteroidsBelt(ResourceLocation registryName, float angularSpeed, float radius, int asteroids, boolean applyBeltWidthScale) {
        this(registryName, angularSpeed, radius, asteroids, applyBeltWidthScale, new ResourceLocation(registryName.getNamespace(), "asteroid/asteroid1"), new ResourceLocation(registryName.getNamespace(), "asteroid/asteroid2"));
    }

    public AsteroidsBelt(ResourceLocation registryName, float angularSpeed, float radius, int asteroids, boolean applyBeltWidthScale, ResourceLocation modelLoc1, ResourceLocation modelLoc2) {
        super(registryName);
        this.angularSpeed = angularSpeed;
        this.modelLoc1 = modelLoc1;
        this.modelLoc2 = modelLoc2;
        this.radius = radius * ImmersiveSpace.SPACE_SCALE;
        this.asteroids = asteroids;
        this.applyBeltWidthScale = applyBeltWidthScale;
    }

    @Override
    public void tick(TickEvent.LevelTickEvent event) {
        super.tick(event);
        angle0 = angle;
        angle += 0.01F + this.angularSpeed;
    }

    @Override
    public void registryModel(ModelEvent.RegisterAdditional event) {
        event.register(this.modelLoc1);
        event.register(this.modelLoc2);
    }

    @Override
    public void render(RenderLevelStageEvent event) {
        RandomSource random = RandomSource.create(2000L);
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource()
                .getBuffer(ImmersiveSpaceRenderTypes.celestialBodies(InventoryMenu.BLOCK_ATLAS));


        event.getPoseStack().translate(getCoordinates(event.getPartialTick()).x, getCoordinates(event.getPartialTick()).y, getCoordinates(event.getPartialTick()).z);
        event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(Mth.lerp(event.getPartialTick(), angle0, angle)/10)));

        for (int i = 0; i < asteroids; ++i) {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(random.nextBoolean() ? modelLoc1 : modelLoc2);
            Vec3 position = new Vec3(0, (2 - 4 * random.nextFloat()) * ImmersiveSpace.SPACE_SCALE * 25, radius + (applyBeltWidthScale ? (asteroids / 20) : 10) * random.nextFloat() * ImmersiveSpace.SPACE_SCALE * 10);
            event.getPoseStack().pushPose();
            event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().translate(position.x / 2, position.y / 2, position.z / 2);
            event.getPoseStack().mulPose(new Quaternionf().rotationZ((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().mulPose(new Quaternionf().rotationX((float) Math.toRadians(random.nextFloat() * 360)));

            event.getPoseStack().scale((applyBeltWidthScale ? 2 : 1) * 50 * ImmersiveSpace.SPACE_SCALE, (applyBeltWidthScale ? 2 : 1) * 50 * ImmersiveSpace.SPACE_SCALE, (applyBeltWidthScale ? 2 : 1) * 50 * ImmersiveSpace.SPACE_SCALE);

            for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, null)) {
                vertexConsumer.putBulkData(event.getPoseStack().last(), quad, 1,1,1, LevelRenderer.getLightColor(Minecraft.getInstance().level, new BlockPos((int) getCoordinates(event.getPartialTick()).x, (int) getCoordinates(event.getPartialTick()).y, (int) getCoordinates(event.getPartialTick()).z)), OverlayTexture.NO_OVERLAY);
            }
            event.getPoseStack().popPose();
        }
    }
}
