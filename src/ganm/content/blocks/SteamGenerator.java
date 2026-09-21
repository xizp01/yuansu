package ganm.content.blocks;

import mindustry.type.*;
import mindustry.world.Block;
import mindustry.content.*;
import ganm.content.liquids.Steam;
import ganm.world.blocks.MultiRecipeCrafter;

/**
 * 蒸汽发生器（工业蒸汽锅炉，多配方）
 * 支持两种加热方式：
 * 配方1：燃料加热（水+煤 → 水蒸气，产量高，不需要电）
 * 配方2：电加热（水+电力 → 水蒸气，产量低，清洁方便）
 * 所属星球：埃里克尔、塞普罗通用
 */
public class SteamGenerator {
    public static Block block;

    public static void load() {
        block = new MultiRecipeCrafter("steam-generator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 60,
                Items.lead, 40,
                Items.titanium, 25,
                Items.silicon, 30,
                Items.metaglass, 15
            ));
            size = 2;
            health = 250;
            liquidCapacity = 40f;
            itemCapacity = 20;

            // 工业锅炉效果：火焰+蒸汽
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.15f;
            updateEffectSpread = 8f;
            warmupSpeed = 0.04f;

            // 双星球通用
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);

            // ========== 多配方定义 ==========
            recipes.add(
                // 配方1：燃料加热（水+煤 → 水蒸气，产量高，不需要电）
                new Recipe()
                    .items(ItemStack.with(Items.coal, 2))
                    .liquids(new LiquidStack(Liquids.water, 10f))
                    .outputLiquid(new LiquidStack(Steam.liquid, 10f))
                    .craftTime(50f)
            );

            recipes.add(
                // 配方2：电加热（水+电力 → 水蒸气，产量低，清洁方便）
                new Recipe()
                    .liquids(new LiquidStack(Liquids.water, 10f))
                    .outputLiquid(new LiquidStack(Steam.liquid, 8f))
                    .craftTime(60f)
            );
        }};
    }
}
