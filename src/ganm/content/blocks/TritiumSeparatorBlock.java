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
/**
 * 氚气分离机（自定义方块类）
 * 运行时释放氚气放射性粒子，粒子飘散过程中碰到单位会施加辐射状态。
 */
public class TritiumSeparatorBlock extends GenericCrafter {
    public float particleSpawnChance = 0.4f;
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
            // 机器运行时释放氚气放射性粒子
            if (efficiency > 0 && Yuansu.tritiumParticles.size < Yuansu.MAX_PARTICLES) {
                if (Mathf.random() < particleSpawnChance) {
                    // 在机器周围随机位置生成粒子
                    float px = x + Mathf.range(size * 3.5f);
                    float py = y + Mathf.range(size * 3.5f);
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
