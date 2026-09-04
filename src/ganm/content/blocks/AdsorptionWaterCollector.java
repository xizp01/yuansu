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
 * 吸附式空气集水器
 * 使用硅胶/氯化锂等干燥剂吸附空气中的水蒸气，再加热解吸冷凝成水。
 * 适合干旱星球，需要消耗硅作为干燥剂，能耗较低。
 * 所属星球：埃里克尔、塞普罗
 */
public class AdsorptionWaterCollector {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("adsorption-water-collector") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 60,
                Items.lead, 50,
                Items.titanium, 30,
                Items.silicon, 35,
                Items.metaglass, 20,
                Items.plastanium, 15
            ));
            size = 2;
            health = 260;
            craftTime = 120f;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            liquidCapacity = 25f;
            outputLiquid = new LiquidStack(Liquids.water, 3f);
            consumeItem(Items.sand, 5); // 硅/沙子作为干燥剂原料
            consumePower(1.5f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.06f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.015f;
        }};
    }
}
