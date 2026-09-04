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
 * 冷凝式空气制水机
 * 通过制冷冷凝，从空气中提取水蒸气凝结成水。
 * 耗电量较高，适合潮湿星球，干燥星球效率较低。
 * 所属星球：埃里克尔、塞普罗
 */
public class CondensationWaterCollector {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("condensation-water-collector") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 50,
                Items.lead, 40,
                Items.titanium, 20,
                Items.silicon, 25,
                Items.metaglass, 15
            ));
            size = 2;
            health = 220;
            craftTime = 80f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 20f;
            outputLiquid = new LiquidStack(Liquids.water, 2f);
            consumePower(3.0f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.08f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.02f;
        }};
    }
}
