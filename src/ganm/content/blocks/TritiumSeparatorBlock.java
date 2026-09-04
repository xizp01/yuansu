package ganm.content.blocks;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.Yuansu;
import ganm.content.liquids.Tritium;
import ganm.content.entities.TritiumParticle;
import ganm.content.entities.TritiumGasCloud;
/**
 * 氚气分离机（自定义方块类）
 * 运行时释放氚气气体云，气体云持续给范围内单位施加辐射状态。
 * 完全按照DeepSeek方案实现。
 */
public class TritiumSeparatorBlock extends GenericCrafter {
    public float cloudSpawnChance = 0.08f; // 每帧8%概率生成气体云
    public float particleSpawnChance = 0.5f;
    public TritiumSeparatorBlock(String name) {
        super(name);
        requirements(Category.crafting, ItemStack.with(
            Items.copper, 50,
            Items.lead, 40,
            Items.titanium, 25,
            Items.silicon, 20,
            Items.metaglass, 15,
            Items.surgeAlloy, 10
        ));
        size = 2;
        health = 240;
        craftTime = 360f;
        hasPower = true;
        hasLiquids = true;
        liquidCapacity = 30f;
        outputLiquid = new LiquidStack(Tritium.liquid, 0.5f);
        consumeLiquid(Liquids.hydrogen, 30f);
        consumePower(2.0f);
        shownPlanets.add(Planets.erekir);
        shownPlanets.add(Planets.serpulo);
        craftEffect = Fx.vapor;
        updateEffect = Fx.steam;
        updateEffectChance = 0.04f;
        updateEffectSpread = 5f;
        warmupSpeed = 0.01f;
    }
    public class TritiumSeparatorBuild extends GenericCrafterBuild {
        @Override
        public void update() {
            super.update();
            if (efficiency > 0) {
                // 生成气体云（DeepSeek核心：气体云自己施加辐射）
                if (Yuansu.tritiumGasClouds.size < Yuansu.MAX_CLOUDS && Mathf.random() < cloudSpawnChance) {
                    float angle = Mathf.random(360f);
                    float dist = size * 2f + Mathf.random(10f);
                    float cx = x + Mathf.cosDeg(angle) * dist;
                    float cy = y + Mathf.sinDeg(angle) * dist;
                    Yuansu.tritiumGasClouds.add(new TritiumGasCloud(cx, cy));
                }
                // 生成粒子（仅视觉效果）
                if (Yuansu.tritiumParticles.size < Yuansu.MAX_PARTICLES && Mathf.random() < particleSpawnChance) {
                    float angle = Mathf.random(360f);
                    float dist = size * 3f + Mathf.random(15f);
                    float px = x + Mathf.cosDeg(angle) * dist;
                    float py = y + Mathf.sinDeg(angle) * dist;
                    Yuansu.tritiumParticles.add(new TritiumParticle(px, py));
                }
            }
        }
        @Override
        public void draw() {
            super.draw();
            // 机器运行时绘制微弱的辐射光晕
            if (efficiency > 0) {
                float pulse = 0.5f + 0.5f * Mathf.sin(Time.time * 0.4f);
                float radius = size * 4f * (0.9f + 0.1f * pulse);
                Draw.color(57f / 255f, 1f, 20f / 255f, 0.05f + 0.03f * pulse);
                Fill.circle(x, y, radius);
                Draw.reset();
            }
        }
    }
}
