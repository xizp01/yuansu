package ganm.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class YuansuLiquids {
    public static Liquid protium;

    public static void load() {
        protium = new Liquid("protium", Color.valueOf("a8b8f5")) {{
            gas = true;
            flammability = 1.2f;
            viscosity = 0.1f;
            temperature = 0.2f;
            heatCapacity = 0.3f;
            barColor = Color.valueOf("8a9ae0");
        }};
    }
}
