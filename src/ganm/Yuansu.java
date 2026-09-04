package ganm;
import arc.util.*;
import arc.Events;
import arc.struct.Seq;
import arc.graphics.g2d.*;
import arc.math.*;
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
    public static final int MAX_PARTICLES = 200;
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
            // 每隔30帧（0.5秒）扫描一次所有存储氚气的建筑
            if (Time.time % 30 == 0) {
                Groups.build.each(building -> {
                    if (building.block != null && building.block.hasLiquids && building.liquids != null) {
                        if (building.liquids.get(Tritium.liquid) > 0.5f) {
                            if (tritiumParticles.size < MAX_PARTICLES) {
                                float px = building.x + Mathf.range(building.block.size * 3f);
                                float py = building.y + Mathf.range(building.block.size * 3f);
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
                Draw.color(57f / 255f, 1f, 20f / 255f, alpha * 0.25f);
                Fill.circle(p.x, p.y, p.radius + 2f);
                // 核心
                Draw.color(57f / 255f, 1f, 20f / 255f, alpha * 0.7f);
                Fill.circle(p.x, p.y, p.radius * 0.6f);
            }
            Draw.reset();
        });
    }
}
