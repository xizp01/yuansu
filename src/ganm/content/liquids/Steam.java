package ganm.content.liquids;
import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;
/**
 * 水蒸气
 * 高温气体，泄漏后会在周围形成蒸汽，单位靠近会被烫伤。
 * 所属星球：埃里克尔、塞普罗
 */
public class Steam {
    public static Liquid liquid;
    public static void load() {
        liquid = new Liquid("steam", Color.valueOf("cccccc")) {{
            gas = true; // 气体
            flammability = 0f;
            viscosity = 0.1f;
            temperature = 1.0f; // 高温
            heatCapacity = 0.5f;
            barColor = Color.valueOf("aaaaaa");
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
