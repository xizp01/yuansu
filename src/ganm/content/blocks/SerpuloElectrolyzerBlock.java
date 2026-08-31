package ganm.content.blocks;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Oxygen;

/**
 * 塞普罗电解制氢机（自定义方块类）
 * 电解水同时产出氢气和氧气（比例 2:1）。
 */
public class SerpuloElectrolyzerBlock extends GenericCrafter {
    public float oxygenOutput = 2.5f;

    public SerpuloElectrolyzerBlock(String name) {
        super(name);
        requirements(Category.crafting, ItemStack.with(
            Items.copper, 40,
            Items.lead, 30,
            Items.titanium, 15,
            Items.silicon, 20
        ));
        size = 2;
        health = 200;
        craftTime = 60f;
        hasPower = true;
        hasLiquids = true;
        liquidCapacity = 30f;
        outputLiquid = new LiquidStack(Liquids.hydrogen, 5f);
        consumeLiquid(Liquids.water, 10f);
        consumePower(2.0f);
        shownPlanets.add(Planets.serpulo);
        craftEffect = Fx.vapor;
        updateEffect = Fx.steam;
        updateEffectChance = 0.1f;
        updateEffectSpread = 6f;
        warmupSpeed = 0.03f;
    }

    public class SerpuloElectrolyzerBuild extends GenericCrafterBuild {
        @Override
        public void craft() {
            super.craft();
            // 水电解：2H2O → 2H2 + O2，同时产出氧气
            liquids.add(Oxygen.liquid, oxygenOutput);
        }
    }
}
