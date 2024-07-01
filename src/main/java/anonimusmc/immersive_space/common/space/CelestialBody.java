package anonimusmc.immersive_space.common.space;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class CelestialBody {
    private static List<CelestialBody> CELESTIAL_BODIES = new ArrayList<>();
    protected Vec3 coordinates;
    protected Vec3 coordinatesO;

    public static ExitableBody getExitableBodyFromDimension(ResourceKey<Level> dim){
        ExitableBody exitableBody = null;
        for (int i = 0; i < CELESTIAL_BODIES.size(); i++) {
            if(CELESTIAL_BODIES.get(i) instanceof ExitableBody && ((ExitableBody)CELESTIAL_BODIES.get(i)).isFromDimension(dim))
                exitableBody = (ExitableBody)CELESTIAL_BODIES.get(i);
        }
        return exitableBody;
    }

    public static List<CelestialBody> getCelestialBodies() {
        return ImmutableList.copyOf(CELESTIAL_BODIES);
    }

    public static <T extends CelestialBody> T registerCelestialBody(T celestialBody){
        CELESTIAL_BODIES.add(celestialBody);
        return celestialBody;
    }

    protected final ResourceLocation registryName;

    public CelestialBody(ResourceLocation registryName) {
        this.registryName = registryName;
        coordinates = Vec3.ZERO;
        coordinatesO = Vec3.ZERO;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void registryModel(ModelEvent.RegisterAdditional event);

    public void tick(TickEvent.LevelTickEvent event){
        coordinatesO = coordinates;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(RenderLevelStageEvent event);

    public Vec3 getCoordinates() {
        return coordinates;
    }

    public Vec3 getCoordinates(float partialTick) {
        return new Vec3(Mth.lerp(partialTick, coordinatesO.x, coordinates.x),Mth.lerp(partialTick, coordinatesO.y, coordinates.y),Mth.lerp(partialTick, coordinatesO.z, coordinates.z));
    }
}
