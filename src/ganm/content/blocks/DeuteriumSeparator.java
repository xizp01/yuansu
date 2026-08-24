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
import ganm.content.liquids.Deuterium;

/**
 * 氘气分离机
 * 通过多级精馏工艺，从氢气中提取稀有氘气，效率较低。
 * 所属星球：埃里克尔
 */
public class DeuteriumSeparator {
    public static Block block;

    public static void load() {
        block = new GenericCrafter("deuterium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 40,
                Items.lead, 30,
                Items.titanium, 15,
                Items.silicon, 15,
                Items.metaglass, 10
            ));
            size = 2;
            health = 200;
            craftTime = 180f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            outputLiquid = new LiquidStack(Deuterium.liquid, 1.5f);
            consumeLiquid(Liquids.hydrogen, 6f);
            consumePower(1.5f);
            shownPlanets.add(Planets.erekir);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.05f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.015f;
        }};
    }
}
