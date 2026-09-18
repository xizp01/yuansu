package ganm.content.liquids;
import arc.graphics.Color;
import mindustry.type.Liquid;
import mindustry.content.Planets;
import ganm.content.status.Scalding;
/**
 * 水蒸气
 * 高温水汽，泄漏后在地面形成水坑，站在上面的单位会被烫伤。
 * 所属星球：埃里克尔、塞普罗
 */
public class Steam {
    public static Liquid liquid;
    public static void load() {
        liquid = new Liquid("steam", Color.valueOf("cccccc")) {{
            gas = false; // 关键：设为液体才会形成水坑
            flammability = 0f;
            viscosity = 0.2f;
            temperature = 1.0f; // 高温
            heatCapacity = 0.8f;
            barColor = Color.valueOf("aaaaaa");
            effect = Scalding.effect; // 站在水坑上施加烫伤
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
