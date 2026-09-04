package ganm.content.items;
import arc.graphics.Color;
import mindustry.type.Item;
import mindustry.content.Planets;
/**
 * 氯化锂干燥剂
 * 吸湿性盐，极低湿度（RH 20%）仍能捕获水汽，再生温度较高。
 * 用于氯化锂复合吸附集水器的消耗品，需要锂化工产业链。
 * 所属星球：埃里克尔、塞普罗
 */
public class LithiumChloride {
    public static Item item;
    public static void load() {
        item = new Item("lithium-chloride", Color.valueOf("d8d8e8")) {{
            hardness = 2;
            cost = 2.5f;
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
