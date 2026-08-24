package ganm;

import arc.util.*;
import mindustry.mod.*;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.tech.ErekirTechTree;

/**
 * 元素模组主类
 * 内容：氕气、氘气、氚气及对应分离机
 * 适配星球：埃里克尔
 */
public class Yuansu extends Mod {

    public Yuansu() {
        Log.info("Loaded Yuansu constructor.");
    }

    @Override
    public void loadContent() {
        // 气体
        Protium.load();
        Deuterium.load();
        Tritium.load();
        // 工厂
        ProtiumSeparator.load();
        DeuteriumSeparator.load();
        TritiumSeparator.load();
        Log.info("Yuansu mod content loaded.");
    }

    @Override
    public void init() {
        // 科技树
        ErekirTechTree.load();
    }
}
