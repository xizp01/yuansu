package ganm;
import arc.util.*;
import arc.Events;
import arc.struct.Seq;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.mod.*;
import mindustry.gen.Groups;
import mindustry.type.LiquidStack;
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
import ganm.content.entities.TritiumParticle;
import ganm.tech.ErekirTechTree;
import ganm.tech.SerpuloTechTree;
/**
 * 元素模组主类
 * 内容：氕气、氘气、氚气及对应分离机，塞普罗制氢机，辐射状态
 * 适配星球：埃里克尔、塞普罗
 */
public class Yuansu extends Mod {
    // 氚气放射性粒子列表
    public static Seq<TritiumParticle> tritiumParticles = new Seq<>();
    // 粒子数量上限，防止卡顿
    public static final int MAX_PARTICLES = 300;
    // 扫描计数器
    private static int scanTimer = 0;
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
        // 氚气粒子更新 + 扫描存储氚气的建筑
        Events.run(Trigger.update, () -> {
            // 每隔15帧扫描一次所有存储氚气的建筑
            scanTimer++;
            if (scanTimer >= 15) {
                scanTimer = 0;
                Groups.build.each(building -> {
                    if (building.liquids != null && building.block != null && building.block.hasLiquids) {
                        // 遍历建筑中的所有液体，检测氚气
                        boolean hasTritium = false;
                        for (LiquidStack stack : building.liquids) {
                            if (stack.liquid == Tritium.liquid && stack.amount > 0.01f) {
                                hasTritium = true;
                                break;
                            }
                        }
                        // 有氚气就生成粒子，存储量越大粒子越多
                        if (hasTritium && tritiumParticles.size < MAX_PARTICLES) {
                            int count = 1 + (int)(building.liquids.get(Tritium.liquid) / 5f);
                            for (int i = 0; i < Math.min(count, 3); i++) {
                                float angle = Mathf.random(360f);
                                float dist = building.block.size * 4f + Mathf.random(8f);
                                float px = building.x + Mathf.cosDeg(angle) * dist;
                                float py = building.y + Mathf.sinDeg(angle) * dist;
                                tritiumParticles.add(new TritiumParticle(px, py));
                            }
                        }
                    }
                });
            }
            // 更新粒子
            for (int i = tritiumParticles.size - 1; i >= 0; i--) {
                TritiumParticle p = tritiumParticles.get(i);
                p.update();
                if (p.isDead()) {
                    tritiumParticles.remove(i);
                }
            }
        });
        // 氚气粒子绘制
        Events.run(Trigger.draw, () -> {
            for (TritiumParticle p : tritiumParticles) {
                float alpha = Mathf.clamp(p.life / p.maxLife);
                // 外层光晕
                Draw.color(57f / 255f, 1f, 20f / 255f, alpha * 0.35f);
                Fill.circle(p.x, p.y, p.radius + 3f);
                // 核心
                Draw.color(150f / 255f, 1f, 100f / 255f, alpha * 0.9f);
                Fill.circle(p.x, p.y, p.radius * 0.7f);
            }
            Draw.reset();
        });
    }
}
