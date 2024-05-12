package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.ImmersiveSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class CelestialBodies {

    public static Star SUN;
    public static Planet MERCURY;
    public static Planet VENUS;
    public static Planet EARTH;
    public static Satellite MOON;
    public static Planet MARS;
    public static Planet JUPITER;
    public static Planet SATURN;
    public static Planet URANUS;
    public static Planet NEPTUNE;
    public static Planet PLUTO;

    public static void registerBodies() {
        SUN = CelestialBody.registerCelestialBody(new Star(new ResourceLocation(ImmersiveSpace.MOD_ID, "sun"), new Vec3(0, 0, 0), new Vec3(10, 10, 10)));
        MERCURY = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "mercury"), SUN, new Vec3(0, 0, 12), 0.08264F, 0.01F, new Vec3(1, 1, 1)));
        VENUS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "venus"), SUN, new Vec3(0, 0, 22), 0.03232F, 0.01F, new Vec3(2, 2, 2)));
        EARTH = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "earth"), SUN, new Vec3(0, 0, 32), 0.01992F, 0.01F, new Vec3(2, 2, 2)));
        MOON = CelestialBody.registerCelestialBody(new Satellite(new ResourceLocation(ImmersiveSpace.MOD_ID, "moon"), EARTH, new Vec3(0,0,3),0.01F, new Vec3(0.5,0.5,0.5)));
        MARS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "mars"), SUN, new Vec3(0, 0, 37), 0.01059F, 0.01F, new Vec3(1.5, 1.5, 1.5)));
        //asteroids
        JUPITER = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "jupiter"), SUN, new Vec3(0, 0, 50), 0.001673F, 0.01F, new Vec3(4, 4, 4)));
        SATURN = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "saturn"), SUN, new Vec3(0, 0, 65), 0.0009294F, 0.01F, new Vec3(3, 3, 3)));
        URANUS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "uranus"), SUN, new Vec3(0, 0, 80), 0.000237F, 0.01F, new Vec3(3, 3, 3)));
        NEPTUNE = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "neptune"), SUN, new Vec3(0, 0, 95), 0.0001208F, 0.01F, new Vec3(3, 3, 3)));
        PLUTO = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "pluto"), SUN, new Vec3(0, 0, 110), 0.00009F, 0.01F, new Vec3(0.75, 0.75, 0.75)));
    }
}
