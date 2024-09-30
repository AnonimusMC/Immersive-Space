package anonimusmc.immersive_space.client.screen;

import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LandingCoordinatesScreen extends Screen {

    private Rocket rocket;
    public LandingCoordinatesScreen(Component pTitle, Rocket rocket) {
        super(pTitle);
        this.rocket = rocket;
    }
}
