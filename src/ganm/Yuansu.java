package ganm;
import arc.util.*;
import arc.Events;
import mindustry.mod.*;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.liquids.Oxygen;
import ganm.content.liquids.Steam;
import ganm.content.status.Scalding;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.content.blocks.SerpuloElectrolyzer;
import ganm.content.blocks.SteamConduit;
import ganm.tech.ErekirTechTree;
import ganm.tech.SerpuloTechTree;
import mindustry.Vars;
/**
 * 元素模组主类
 * 内容：氕气、氘气、氚气及对应分离机，塞普罗制氢机
 * 适配星球：埃里克尔、塞普罗
 */
public class Yuansu extends Mod {
    private int tickCounter = 0;
    public Yuansu() {
        Log.info("Loaded Yuansu constructor.");
    }
    @Override
    public void loadContent() {
        // 状态效果
        Scalding.load();
        // 气体/液体
        Oxygen.load();
        Protium.load();
        Deuterium.load();
        Tritium.load();
        Steam.load();
        // 工厂
        ProtiumSeparator.load();
        DeuteriumSeparator.load();
        TritiumSeparator.load();
        SerpuloElectrolyzer.load();
        // 蒸汽管道
        new SteamConduit("steam-conduit");
        Log.info("Yuansu mod content loaded.");
    }
    @Override
    public void init() {
        // 科技树（双星球）
        ErekirTechTree.load();
        SerpuloTechTree.load();
        // 全局蒸汽烫伤检测：每帧触发，每10帧实际检测一次
        Events.on(EventType.Trigger.update, () -> {
            tickCounter++;
            if (tickCounter % 10 != 0) return;
            // 全局扫描所有单位
            for (Unit unit : Groups.unit) {
                if (!unit.isValid()) continue;
                // 检查单位周围3格内有没有存储水蒸气的建筑
                boolean nearSteam = false;
                int ux = (int)(unit.x / 8);
                int uy = (int)(unit.y / 8);
                for (int x = ux - 3; x <= ux + 3; x++) {
                    for (int y = uy - 3; y <= uy + 3; y++) {
                        var tile = Vars.world.tile(x, y);
                        if (tile == null) continue;
                        var build = tile.build;
                        if (build == null || build.liquids == null) continue;
                        if (build.liquids.currentAmount() <= 0) continue;
                        if (build.liquids.current() == Steam.liquid) {
                            nearSteam = true;
                            break;
                        }
                    }
                    if (nearSteam) break;
                }
                if (nearSteam) {
                    unit.apply(Scalding.effect, 60f); // 持续1秒
                }
            }
        });
        Log.info("Steam scald detection initialized.");
    }
}
