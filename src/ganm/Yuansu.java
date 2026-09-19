package ganm;
import arc.util.*;
import arc.Events;
import arc.struct.Seq;
import mindustry.mod.*;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.Liquid;
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
    // D-A方案：缓存当前满足烫伤条件的建筑
    private Seq<Building> hotSteamBuilds = new Seq<>();
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
        // 蒸汽管道（安全运输，不造成烫伤）
        SteamConduit.load();
        Log.info("Yuansu mod content loaded.");
    }
    @Override
    public void init() {
        // 科技树（双星球）
        ErekirTechTree.load();
        SerpuloTechTree.load();

        // 【D-A方案】低频率筛选轮询 + Seq缓存有效建筑
        Events.run(EventType.Trigger.update, () -> {
            tickCounter++;
            // 每10帧执行一轮完整检测（每秒6次）
            if (tickCounter % 10 != 0) return;
            tickCounter = 0; // 计数器归零，防止int无限上涨

            hotSteamBuilds.clear(); // 清空上一轮的数据

            // ==========第一步：遍历全图所有建筑，筛选符合条件的建筑==========
            for (Building build : Groups.build) {
                // 1.建筑无效：已经被拆除、销毁，直接跳过
                if (!build.isValid()) continue;
                // 2.建筑没有流体组件（墙壁、炮台等）直接跳过
                if (build.liquids == null) continue;

                float steamAmount = build.liquids.currentAmount();
                Liquid currentLiquid = build.liquids.current();

                // 3.条件1：流体必须是水蒸气，并且存量大于0
                if (currentLiquid != Steam.liquid || steamAmount <= 0f) continue;

                // 4.条件2：排除蒸汽管道，该管道装蒸汽也不会烫伤
                if (build.block.name.equals("steam-conduit")) continue;

                // 全部条件满足 → 加入待处理集合
                hotSteamBuilds.add(build);
            }

            // ==========第二步：只遍历筛选出来的少量建筑，执行烫伤==========
            for (Building b : hotSteamBuilds) {
                float radius = 24f; // 24像素 = 3格半径
                float bx = b.x;
                float by = b.y;

                // 遍历范围内的单位施加烫伤
                Groups.unit.each(unit -> {
                    if (!unit.isValid()) return;
                    if (unit.dst(bx, by) < radius) {
                        unit.apply(Scalding.effect, 60f);
                    }
                });
            }
        });

        Log.info("D-A Steam scald system initialized.");
    }
}
