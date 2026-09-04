package ganm.content.items;
import arc.graphics.Color;
import mindustry.type.Item;
import mindustry.content.Planets;
/**
 * 硅胶干燥剂
 * 多孔二氧化硅颗粒，中高湿度环境吸水能力强，再生温度80-120℃。
 * 用于除湿转轮取水器的消耗品。
 * 所属星球：埃里克尔、塞普罗
 */
public class SilicaGel {
    public static Item item;
    public static void load() {
        item = new Item("silica-gel", Color.valueOf("c8b89a")) {{
            hardness = 1;
            cost = 1.2f;
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
