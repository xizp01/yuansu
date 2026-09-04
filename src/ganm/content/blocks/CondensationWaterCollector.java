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
import ganm.content.PlanetHumidity;
/**
 * 冷凝式空气制水机
 * 通过制冷冷凝，从空气中提取水蒸气凝结成水。
 * 耗电量较高，产量受星球湿度影响，潮湿星球效率高。
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
        }} {
            // 自定义Build类：根据星球湿度调整产量
            @Override
            public void update() {
                super.update();
            }
            @Override
            public float getProgressIncrease(float baseEfficiency) {
                // 进度增加量 = 基础进度 × 湿度倍率
                return super.getProgressIncrease(baseEfficiency) * PlanetHumidity.getProductionMultiplier();
            }
        };
    }
}
