package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.client.RenderUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.adastra.common.utils.ModUtils;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Random;

public class AsteroidsBelt extends CelestialBody {

    private final ArrayList<AABB> asteroids;
    private final boolean applyBeltWidthScale;
    private float angle = new Random().nextInt(360);
    private float angle0 = angle;
    private float angularSpeed;
    private float radius;
    private ResourceLocation modelLoc1;
    private ResourceLocation modelLoc2;


    public AsteroidsBelt(ResourceLocation registryName, float angularSpeed, float radius, int asteroids, boolean applyBeltWidthScale) {
        super(registryName);
        this.angularSpeed = angularSpeed;
        modelLoc1 = new ResourceLocation(registryName.getNamespace(), "asteroid/asteroid1");
        modelLoc2 = new ResourceLocation(registryName.getNamespace(), "asteroid/asteroid2");
        this.radius = radius;
        this.asteroids = new ArrayList<>(asteroids);
        this.applyBeltWidthScale = applyBeltWidthScale;
        RandomSource random = RandomSource.create(1000L);
        for (int i = 0; i < asteroids; i++) {
            Vec3 asteroidPosition = new Vec3(asteroids + (random.nextBoolean() ? random.nextInt(50) : -random.nextInt(50)), random.nextBoolean() ? random.nextInt(50) : -random.nextInt(50), random.nextBoolean() ? random.nextInt(50) : -random.nextInt(50)).yRot(7 * i);
            this.asteroids.add(new AABB(asteroidPosition, asteroidPosition.add(random.nextFloat(), random.nextFloat(), random.nextFloat())));
        }
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
        VertexConsumer vertexConsumer = RenderUtils.BUFFER
                .getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));

        event.getPoseStack().translate(getCoordinates(event.getPartialTick()).x, getCoordinates(event.getPartialTick()).y, getCoordinates(event.getPartialTick()).z);
        event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(Mth.lerp(event.getPartialTick(),angle0,angle))));
        for (int i = 0; i < asteroids.size(); ++i) {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(random.nextBoolean() ? modelLoc1 : modelLoc2);
            Vec3 position = new Vec3(0, 2 - 4 * random.nextFloat(), radius + (applyBeltWidthScale ? (asteroids.size() / 20) : 10) * random.nextFloat());
            event.getPoseStack().pushPose();
            event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().translate(position.x / 2, position.y / 2, position.z / 2);
            event.getPoseStack().mulPose(new Quaternionf().rotationZ((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().mulPose(new Quaternionf().rotationY((float) Math.toRadians(random.nextFloat() * 360)));
            event.getPoseStack().mulPose(new Quaternionf().rotationX((float) Math.toRadians(random.nextFloat() * 360)));

            event.getPoseStack().scale(50, 50, 50);
            for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, null)) {
                vertexConsumer.putBulkData(event.getPoseStack().last(), quad, 1, 1, 1, LevelRenderer.getLightColor(Minecraft.getInstance().level, new BlockPos((int) getCoordinates(event.getPartialTick()).x, (int) getCoordinates(event.getPartialTick()).y, (int) getCoordinates(event.getPartialTick()).z)), OverlayTexture.NO_OVERLAY);
            }


            event.getPoseStack().popPose();
        }
    }
}
