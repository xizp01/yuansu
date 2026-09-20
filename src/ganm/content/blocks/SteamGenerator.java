package ganm.content.blocks;

import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Steam;

/**
 * 蒸汽发生器（工业蒸汽锅炉）
 * 通过燃烧煤炭加热水产生高温水蒸气，为蒸汽动力系统提供气源。
 * 现实逻辑：水 + 燃料（煤）→ 高温水蒸气
 * 所属星球：埃里克尔、塞普罗通用
 */
public class SteamGenerator {
    public static Block block;

    public static void load() {
        block = new GenericCrafter("steam-generator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 60,
                Items.lead, 40,
                Items.titanium, 25,
                Items.silicon, 30,
                Items.metaglass, 15
            ));
            size = 2;
            health = 250;
            craftTime = 60f;
            hasItems = true;
            hasLiquids = true;
            hasPower = false; // 燃料加热，不需要电力
            liquidCapacity = 40f;
            itemCapacity = 20;

            // 输入：水10 + 煤2（燃料加热水）
            consumeLiquid(Liquids.water, 10f);
            consumeItems(ItemStack.with(Items.coal, 2));

            // 输出：水蒸气8（水蒸发体积膨胀，有损耗）
            outputLiquid = new LiquidStack(Steam.liquid, 8f);

            // 工业锅炉效果：火焰+蒸汽
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.15f;
            updateEffectSpread = 8f;
            warmupSpeed = 0.04f;

            // 双星球通用
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
