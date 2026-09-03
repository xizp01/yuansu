package ganm.content.liquids;

import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;

/**
 * 氚气（超重氢）
 * 氢的放射性同位素，核聚变的关键燃料，半衰期约12.3年。
 * 具有放射性，其分离机周围会产生辐射区域。
 * 所属星球：埃里克尔、塞普罗
 */
public class Tritium {
    public static Liquid liquid;

    public static void load() {
        liquid = new Liquid("tritium", Color.valueOf("5a6ab0")) {{
            gas = true;
            flammability = 0.8f;
            viscosity = 0.14f;
            temperature = 0.25f;
            heatCapacity = 0.4f;
            radioactivity = 1.5f;
            barColor = Color.valueOf("3a4a90");
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
