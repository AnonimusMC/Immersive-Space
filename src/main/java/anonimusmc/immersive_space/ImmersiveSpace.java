package anonimusmc.immersive_space;

import anonimusmc.immersive_space.client.ShaderInstances;
import anonimusmc.immersive_space.client.space.SpaceDimensionRenderInfo;
import anonimusmc.immersive_space.common.space.CelestialBodies;
import anonimusmc.immersive_space.common.space.CelestialBody;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;

@Mod(ImmersiveSpace.MOD_ID)
public class ImmersiveSpace {
    public static final String MOD_ID = "immersive_space";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<Level> SPACE = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MOD_ID, "space"));

    public static int SPACE_SCALE = 50;

    public ImmersiveSpace() {
        CelestialBodies.registerBodies();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::livingEntityDamage);
        MinecraftForge.EVENT_BUS.addListener(this::worldTick);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::registerDimensionSpecialEffect);
            modEventBus.addListener(this::registerModel);
            modEventBus.addListener(this::registerShader);
            MinecraftForge.EVENT_BUS.addListener(this::renderLevelLast);
        });
    }

    private void worldTick(TickEvent.LevelTickEvent event) {
        CelestialBody.getCelestialBodies().forEach(celestialBody -> celestialBody.tick(event));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @OnlyIn(Dist.CLIENT)
    private void registerModel(ModelEvent.RegisterAdditional event) {
        CelestialBody.getCelestialBodies().forEach(celestialBody -> celestialBody.registryModel(event));
    }

    @OnlyIn(Dist.CLIENT)
    private void registerShader(final RegisterShadersEvent event)  {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(ImmersiveSpace.MOD_ID, "space"), DefaultVertexFormat.NEW_ENTITY), (shaderInstance) -> {
                ShaderInstances.SPACE_SHADERS = shaderInstance;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void registerDimensionSpecialEffect(final RegisterDimensionSpecialEffectsEvent event) {
        event.register(new ResourceLocation(MOD_ID, "space"), new SpaceDimensionRenderInfo());
    }

    @OnlyIn(Dist.CLIENT)
    private void renderLevelLast(final RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY && Minecraft.getInstance().level.dimension() == SPACE) {

            PoseStack matrixStack = event.getPoseStack();
            matrixStack.pushPose();
            Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            matrixStack.translate(-view.x(), -view.y(), -view.z());
            FogRenderer.setupNoFog();
            CelestialBody.getCelestialBodies().forEach(celestialBody -> {
                event.getPoseStack().pushPose();
                celestialBody.render(event);
                event.getPoseStack().popPose();
            });

            matrixStack.popPose();
        }
    }

    private void livingEntityDamage(final LivingAttackEvent event) {
        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && event.getEntity().level().dimension().equals(SPACE))
            event.setCanceled(true);
    }
}
