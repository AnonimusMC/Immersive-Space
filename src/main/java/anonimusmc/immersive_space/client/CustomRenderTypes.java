package anonimusmc.immersive_space.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiFunction;

public class CustomRenderTypes extends RenderType{

    public CustomRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType entityTranslucent(ResourceLocation pLocation) {
        return ENTITY_TRANSLUCENT.apply(pLocation, false);
    }

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_TRANSLUCENT = Util.memoize((p_286156_, p_286157_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286156_, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(p_286157_);
        return create("entity_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    @OnlyIn(Dist.CLIENT)
    public static class CullStateShard extends RenderStateShard.BooleanStateShard {
        public CullStateShard(boolean pUseCull) {
            super("cull", () -> {
                if (!pUseCull) {
                    RenderSystem.disableCull();
                    RenderSystem.disableDepthTest();
                }

            }, () -> {
                if (!pUseCull) {
                    RenderSystem.enableCull();
                    RenderSystem.enableDepthTest();
                }

            }, pUseCull);
        }
    }
}
