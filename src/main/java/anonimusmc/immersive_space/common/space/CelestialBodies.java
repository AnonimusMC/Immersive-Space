package anonimusmc.immersive_space.common.space;

import anonimusmc.immersive_space.ImmersiveSpace;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.api.planets.PlanetApi;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CelestialBodies {

    public static final ResourceKey<Level> MARS_ORBIT = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(AdAstra.MOD_ID, "mars_orbit"));
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
        SUN = CelestialBody.registerCelestialBody(new Star(new ResourceLocation(ImmersiveSpace.MOD_ID, "sun"), new Vec3(0, 0, 0), new Vec3(100, 100, 100)));
        MERCURY = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "mercury"), SUN, new Vec3(0, 0, 120), 0.008264F, 0.01F, new Vec3(10, 10, 10)));
        VENUS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "venus"), SUN, new Vec3(0, 0, 220), 0.003232F, 0.01F, new Vec3(20, 20, 20)));
        EARTH = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "earth"), SUN, new Vec3(0, 0, 320), 0.001992F, 0.01F, new Vec3(20, 20, 20), ()->PlanetApi.API.getPlanet(Level.OVERWORLD)));
        MOON = CelestialBody.registerCelestialBody(new Satellite(new ResourceLocation(ImmersiveSpace.MOD_ID, "moon"), EARTH, new Vec3(0,0,30),0.001F, new Vec3(5,5,5)));
        MARS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "mars"), SUN, new Vec3(0, 0, 370), 0.001059F, 0.01F, new Vec3(15, 15, 15), ()->PlanetApi.API.getPlanet(earth.terrarium.adastra.api.planets.Planet.MARS)));
        //asteroids
        CelestialBody.registerCelestialBody(new AsteroidsBelt(new ResourceLocation(ImmersiveSpace.MOD_ID, "asteroids_belt"),0.001F, 800F, 1000, false));

        JUPITER = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "jupiter"), SUN, new Vec3(0, 0, 500), 0.0001673F, 0.01F, new Vec3(40, 40, 40)));
        SATURN = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "saturn"), SUN, new Vec3(0, 0, 650), 0.00009294F, 0.01F, new Vec3(30, 30, 30)));
        URANUS = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "uranus"), SUN, new Vec3(0, 0, 800), 0.0000237F, 0.01F, new Vec3(30, 30, 30)));
        NEPTUNE = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "neptune"), SUN, new Vec3(0, 0, 950), 0.00001208F, 0.01F, new Vec3(30, 30, 30)));
        PLUTO = CelestialBody.registerCelestialBody(new Planet(new ResourceLocation(ImmersiveSpace.MOD_ID, "pluto"), SUN, new Vec3(0, 0, 110), 0.000009F, 0.01F, new Vec3(07.5, 07.5, 07.5)));
        CelestialBody.registerCelestialBody(new AsteroidsBelt(new ResourceLocation(ImmersiveSpace.MOD_ID, "kuiper_belt"),0.001F, 2500F, 10000, true));
    }
}
