package ganm.content.items;
import arc.graphics.Color;
import mindustry.type.Item;
import mindustry.content.Planets;
/**
 * MOF金属有机框架材料
 * 前沿黑科技材料，超大比表面积（1克可达7000㎡），纳米孔洞精确抓取水分子。
 * 再生温度低（60-80℃），可利用太阳能光热加热，全湿度区间稳定工作。
 * 用于MOF光热空气取水器的高级消耗品。
 * 所属星球：埃里克尔、塞普罗
 */
public class MOFMaterial {
    public static Item item;
    public static void load() {
        item = new Item("mof-material", Color.valueOf("9a7ad8")) {{
            hardness = 4;
            cost = 5.0f;
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
