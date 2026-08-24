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
import ganm.content.liquids.Protium;

/**
 * 氕气分离机
 * 通过电解分离工艺，从氢气中提取高纯度氕气，是同位素分离的基础设备。
 * 所属星球：埃里克尔
 */
public class ProtiumSeparator {
    public static Block block;

    public static void load() {
        block = new GenericCrafter("protium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 30,
                Items.lead, 20,
                Items.titanium, 10,
                Items.silicon, 10
            ));
            size = 2;
            health = 160;
            craftTime = 120f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            outputLiquid = new LiquidStack(Protium.liquid, 3f);
            consumeLiquid(Liquids.hydrogen, 5f);
            consumePower(1.0f);
            shownPlanets.add(Planets.erekir);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.06f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.02f;
        }};
    }
}
