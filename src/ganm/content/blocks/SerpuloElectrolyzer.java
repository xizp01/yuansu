package ganm.content.blocks;

import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.Fx;

/**
 * 塞普罗电解制氢机
 * 通过电解水制取氢气，为塞普罗同位素分离产业链提供原料。
 * 所属星球：塞普罗
 */
public class SerpuloElectrolyzer {
    public static Block block;

    public static void load() {
        block = new GenericCrafter("serpulo-electrolyzer") {{
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
            liquidCapacity = 15f;
            outputLiquid = new LiquidStack(Liquids.hydrogen, 5f);
            consumeLiquid(Liquids.water, 10f);
            consumePower(2.0f);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.1f;
            updateEffectSpread = 6f;
            warmupSpeed = 0.03f;
        }};
    }
}
