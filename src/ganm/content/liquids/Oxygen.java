package ganm.content.liquids;

import mindustry.type.Liquid;

/**
 * 氧气
 * 水电解产物之一，无色无味的气体，支持燃烧和生命活动。
 */
public class Oxygen {
    public static Liquid liquid;

    public static void load() {
        liquid = new Liquid("oxygen") {{
            color = arc.graphics.Color.valueOf("6ec6ff");
            gas = true;
            flammability = 0.3f;
            explosiveness = 0f;
            heatCapacity = 0.5f;
            temperature = 0.5f;
            viscosity = 0.3f;
        }};
    }
}
