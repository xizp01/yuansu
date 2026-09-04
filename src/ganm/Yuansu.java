package ganm;
import arc.util.*;
import mindustry.mod.*;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.liquids.Oxygen;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.content.blocks.SerpuloElectrolyzer;
import ganm.content.blocks.CondensationWaterCollector;
import ganm.content.blocks.AdsorptionWaterCollector;
import ganm.content.blocks.FogWaterCollector;
import ganm.tech.ErekirTechTree;
import ganm.tech.SerpuloTechTree;
/**
 * 元素模组主类
 * 内容：氕气、氘气、氚气及对应分离机，塞普罗制氢机，空气取水设备
 * 适配星球：埃里克尔、塞普罗
 */
public class Yuansu extends Mod {
    public Yuansu() {
        Log.info("Loaded Yuansu constructor.");
    }
    @Override
    public void loadContent() {
        // 气体
        Oxygen.load();
        Protium.load();
        Deuterium.load();
        Tritium.load();
        // 工厂
        ProtiumSeparator.load();
        DeuteriumSeparator.load();
        TritiumSeparator.load();
        SerpuloElectrolyzer.load();
        // 空气取水设备
        CondensationWaterCollector.load();
        AdsorptionWaterCollector.load();
        FogWaterCollector.load();
        Log.info("Yuansu mod content loaded.");
    }
    @Override
    public void init() {
        // 科技树（双星球）
        ErekirTechTree.load();
        SerpuloTechTree.load();
    }
}
