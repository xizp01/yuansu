package ganm.content;

import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;

public class YuansuLiquids {
    public static Liquid protium, deuterium, tritium;

    public static void load() {
        protium = new Liquid("protium", Color.valueOf("a8b8f5")) {{
            gas = true;
            flammability = 1.2f;
            viscosity = 0.1f;
            temperature = 0.2f;
            heatCapacity = 0.3f;
            barColor = Color.valueOf("8a9ae0");
            shownPlanets.add(Planets.erekir);
        }};

        deuterium = new Liquid("deuterium", Color.valueOf("7a8ad0")) {{
            gas = true;
            flammability = 1.0f;
            viscosity = 0.12f;
            temperature = 0.22f;
            heatCapacity = 0.35f;
            barColor = Color.valueOf("5a6ab0");
            shownPlanets.add(Planets.erekir);
        }};

        tritium = new Liquid("tritium", Color.valueOf("5a6ab0")) {{
            gas = true;
            flammability = 0.8f;
            viscosity = 0.14f;
            temperature = 0.25f;
            heatCapacity = 0.4f;
            barColor = Color.valueOf("3a4a90");
            shownPlanets.add(Planets.erekir);
        }};
    }
}
