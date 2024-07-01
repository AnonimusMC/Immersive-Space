package anonimusmc.immersive_space.common.space;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface ExitableBody {

    Vec3 getExitPosition();

    boolean isFromDimension(ResourceKey<Level> dim);
}
