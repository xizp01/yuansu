package ganm.content.liquids;

import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;

/**
 * 氘气（重氢）
 * 氢的稳定同位素，核聚变的主要燃料之一，可用于中子慢化剂和特种化工。
 * 所属星球：埃里克尔、塞普罗
 */
public class Deuterium {
    public static Liquid liquid;

    public static void load() {
        liquid = new Liquid("deuterium", Color.valueOf("7a8ad0")) {{
            gas = true;
            flammability = 1.0f;
            viscosity = 0.12f;
            temperature = 0.22f;
            heatCapacity = 0.35f;
            barColor = Color.valueOf("5a6ab0");
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
