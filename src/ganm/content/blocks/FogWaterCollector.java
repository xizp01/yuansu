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
 * 被动：雾水收集栅格
 * 零耗电被动设备，通过细密网格拦截空气中的雾滴汇聚成水。
 * 只有湿度≥90的多雾环境才工作，湿度不足完全停止产出。
 * 所属星球：埃里克尔、塞普罗
 */
public class FogWaterCollector {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("fog-water-collector") {
            {
                requirements(Category.crafting, ItemStack.with(
                    Items.copper, 15,
                    Items.lead, 10,
                    Items.metaglass, 8
                ));
                size = 1;
                health = 100;
                craftTime = 80f;
                hasPower = false;
                hasLiquids = true;
                liquidCapacity = 8f;
                outputLiquid = new LiquidStack(Liquids.water, 0.6f);
                shownPlanets.add(Planets.erekir);
                shownPlanets.add(Planets.serpulo);
                craftEffect = Fx.vapor;
                updateEffect = Fx.steam;
                updateEffectChance = 0.02f;
                updateEffectSpread = 2f;
            }
            @Override
            public float getProgressIncrease(float baseEfficiency) {
                float humidity = PlanetHumidity.getCurrentHumidity();
                float multiplier = (humidity >= 90f) ? 1.0f : 0f;
                return super.getProgressIncrease(baseEfficiency) * multiplier;
            }
        };
    }
}
