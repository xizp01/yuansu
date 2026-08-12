package example.gas;

import arc.graphics.Color;
import mindustry.type.Gas;

public class Protium extends Gas {
    public Protium(){
        super("protium");

        localizedName = "氕气";
        description = """
宇宙中含量最高的氢同位素气体，由¹H₂分子组成。
高度易燃，遇氧气能够剧烈爆燃。
无法维持核聚变反应，不可作为聚变燃料。
原子核不含中子，完全没有放射性。
        """;

        color = Color.valueOf("#c8e8ff");
        gasColor = Color.valueOf("#e6f4ff");
        barColor = Color.valueOf("#c8e8ff");

        flammability = 1.3f;
        explosiveness = 1.2f;
        viscosity = 0.45f;
        temperature = 25f;
    }
}
