package ganm;
import arc.util.*;
import mindustry.mod.*;
import mindustry.gen.Groups;
import mindustry.game.EventType.Trigger;
import ganm.content.status.Radiation;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.liquids.Oxygen;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.content.blocks.SerpuloElectrolyzer;
import ganm.tech.ErekirTechTree;
import ganm.tech.SerpuloTechTree;
/**
 * 元素模组主类
 * 内容：氕气、氘气、氚气及对应分离机，塞普罗制氢机，辐射状态
 * 适配星球：埃里克尔、塞普罗
 */
public class Yuansu extends Mod {
    public Yuansu() {
        Log.info("Loaded Yuansu constructor.");
    }
    @Override
    public void loadContent() {
        // 状态效果（必须先加载，氚气会引用）
        Radiation.load();
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
        Log.info("Yuansu mod content loaded.");
    }
    @Override
    public void init() {
        // 科技树（双星球）
        ErekirTechTree.load();
        SerpuloTechTree.load();
        // 氚气辐射检测：气体不会形成水坑，主动检测单位所在位置的氚气并施加辐射
        Events.run(Trigger.update, () -> {
            Groups.unit.forEach(unit -> {
                if (unit.tileOn() != null && unit.tileOn().getLiquid() != null) {
                    if (unit.tileOn().getLiquid().liquid == Tritium.liquid) {
                        unit.apply(Radiation.effect, 120f);
                    }
                }
            });
        });
    }
}
