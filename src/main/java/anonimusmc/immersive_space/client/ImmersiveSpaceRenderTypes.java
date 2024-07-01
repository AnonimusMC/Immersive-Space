package anonimusmc.immersive_space.client;

import anonimusmc.immersive_space.ImmersiveSpace;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import foundry.veil.api.client.render.VeilRenderBridge;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class ImmersiveSpaceRenderTypes extends RenderType {

    private static final ShaderStateShard CELESTIAL_BODIES_SHADER_STATE = VeilRenderBridge.shaderState(new ResourceLocation(ImmersiveSpace.MOD_ID, "space"));

    public ImmersiveSpaceRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType celestialBodies(ResourceLocation texture){
        return create("celestial_bodies", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 2097152, false, false, RenderType.CompositeState.builder().setShaderState(CELESTIAL_BODIES_SHADER_STATE).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOverlayState(RenderStateShard.NO_OVERLAY).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setCullState(RenderStateShard.CULL).createCompositeState(false));
    }

}
