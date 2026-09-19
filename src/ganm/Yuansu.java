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
            // 多人联机：只在服务端执行逻辑，单机时Vars.net.server()同样为true
            if (!Vars.net.server()) return;

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

                // 3.条件1：流体必须是水蒸气，并且存量大于阈值2
                if (currentLiquid != Steam.liquid || steamAmount <= 2f) continue;

                // 4.条件2：排除蒸汽管道，该管道装蒸汽也不会烫伤
                if (build.block.name.equals("steam-conduit")) continue;

                // 全部条件满足 → 加入待处理集合
                hotSteamBuilds.add(build);
            }

            // ==========第二步：只遍历筛选出来的少量建筑，执行烫伤==========
            for (Building b : hotSteamBuilds) {
                float radius = 24f; // 24像素 = 3格半径

                // intersect：引擎空间索引，只取出这个矩形范围内的单位
                Groups.unit.intersect(
                    b.x - radius,
                    b.y - radius,
                    radius * 2f,
                    radius * 2f,
                    unit -> {
                        if (unit.isValid()) {
                            // 给单位施加烫伤状态，持续60帧=1秒
                            unit.apply(Scalding.effect, 60f);
                        }
                    }
                );
            }
        });

        Log.info("D-A Steam scald system initialized.");
    }
}
