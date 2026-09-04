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
 * 雾水收集网
 * 被动式集水设备，通过细密网格拦截空气中的雾滴汇聚成水。
 * 零耗电，产量受湿度影响极大，低湿度环境几乎不产水。
 * 所属星球：埃里克尔、塞普罗
 */
public class FogWaterCollector {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("fog-water-collector") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 20,
                Items.lead, 15,
                Items.metaglass, 10
            ));
            size = 1;
            health = 120;
            craftTime = 180f;
            hasPower = false; // 被动设备，不需要电力
            hasLiquids = true;
            liquidCapacity = 10f;
            outputLiquid = new LiquidStack(Liquids.water, 1f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.03f;
            updateEffectSpread = 3f;
        }} {
            // 自定义Build类：雾水收集受湿度影响极大
            @Override
            public float getProgressIncrease(float baseEfficiency) {
                float humidity = PlanetHumidity.getCurrentHumidity();
                // 湿度低于20时完全不产水（没有雾）
                // 湿度20-100时，倍率从0.2线性增加到1.8
                float multiplier;
                if (humidity < 20f) {
                    multiplier = 0f;
                } else {
                    multiplier = 0.2f + ((humidity - 20f) / 80f) * 1.6f;
                }
                return super.getProgressIncrease(baseEfficiency) * multiplier;
            }
        };
    }
}
