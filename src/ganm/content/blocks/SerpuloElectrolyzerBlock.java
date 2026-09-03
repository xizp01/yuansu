package ganm.content.blocks;

import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Oxygen;

/**
 * 塞普罗电解制氢机
 * 电解水同时产出氢气和氧气（比例 2:1），使用 GenericCrafter 的 outputLiquids 实现双输出。
 */
public class SerpuloElectrolyzerBlock extends GenericCrafter {
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
        // 双液体输出：氢气 + 氧气（比例 2:1），和原版电解机一样的机制
        outputLiquids = new LiquidStack[]{
            new LiquidStack(Liquids.hydrogen, 5f),
            new LiquidStack(Oxygen.liquid, 2.5f)
        };
        consumeLiquid(Liquids.water, 10f);
        consumePower(2.0f);
        shownPlanets.add(Planets.serpulo);
        craftEffect = Fx.vapor;
        updateEffect = Fx.steam;
        updateEffectChance = 0.1f;
        updateEffectSpread = 6f;
        warmupSpeed = 0.03f;
    }
}
