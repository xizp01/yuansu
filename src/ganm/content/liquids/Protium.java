package ganm.content.liquids;

import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;

/**
 * 氕气（轻氢）
 * 基础能源气体，可做燃料、化工原料；聚变反应很难直接使用，需要分离氘氚。
 * 所属星球：埃里克尔
 */
public class Protium {
    public static Liquid liquid;

    public static void load() {
        liquid = new Liquid("protium", Color.valueOf("a8b8f5")) {{
            gas = true;
            flammability = 1.2f;
            viscosity = 0.1f;
            temperature = 0.2f;
            heatCapacity = 0.3f;
            barColor = Color.valueOf("8a9ae0");
            shownPlanets.add(Planets.erekir);
        }};
    }
}
