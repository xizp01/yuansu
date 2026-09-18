package ganm;
import arc.util.*;
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
import ganm.content.blocks.SteamDetectorBlock;
import ganm.tech.ErekirTechTree;
import ganm.tech.SerpuloTechTree;
import arc.Core;
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
        // 全局检测方块
        SteamDetectorBlock detector = new SteamDetectorBlock("steam-detector");
        Log.info("Yuansu mod content loaded.");
    }
    @Override
    public void init() {
        // 科技树（双星球）
        ErekirTechTree.load();
        SerpuloTechTree.load();
        // 自动放置蒸汽检测器到地图中心
        try {
            int cx = Vars.world.width() / 2;
            int cy = Vars.world.height() / 2;
            var tile = Vars.world.tile(cx, cy);
            if (tile != null) {
                tile.setBlock(Vars.content.block("steam-detector"));
                Log.info("Steam detector placed at " + cx + ", " + cy);
            }
        } catch (Exception e) {
            Log.err("Failed to place steam detector: " + e.getMessage());
        }
    }
}
