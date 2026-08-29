package ganm.content.blocks;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Tritium;
import ganm.content.status.Radiation;

/**
 * 氚气分离机（自定义方块类）
 * 运行时周围会产生辐射区域，对范围内单位持续施加辐射状态。
 */
public class TritiumSeparatorBlock extends GenericCrafter {
    public float radiationRange = 48f;
    public float radiationDuration = 180f;

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
            // 机器运行时给周围单位施加辐射
            if (efficiency > 0) {
                Groups.unit.each((mindustry.gen.Unit unit) -> {
                    if (unit.within(x, y, radiationRange)) {
                        unit.apply(Radiation.effect, radiationDuration);
                    }
                });
            }
        }
    }
}
