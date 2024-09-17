package anonimusmc.immersive_space.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class ImmersiveSpaceRenderTypes extends RenderType {

    private static final ShaderStateShard CELESTIAL_BODIES_SHADER_STATE = new RenderStateShard.ShaderStateShard(()->ShaderInstances.SPACE_SHADERS);

    public ImmersiveSpaceRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType celestialBodies(ResourceLocation texture){
//        return entityTranslucent(texture);
//    }
//    public static RenderType celestialBodies(ResourceLocation texture){
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(CELESTIAL_BODIES_SHADER_STATE).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return create("celestial_bodies", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    }

}
