package ganm.content.blocks;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.Fx;
import ganm.content.PlanetHumidity;
import ganm.content.items.MOFMaterial;
/**
 * 四代：MOF光热空气取水器
 * 金属有机框架材料，超大比表面积精确抓取水分子，太阳能光热加热解吸。
 * 全湿度区间稳定工作，耗电极低（仅风机），外星前哨终极空气取水方案。
 * 所属星球：埃里克尔、塞普罗
 */
public class MOFWaterCollector {
    public static Block block;
    public static void load() {
        block = new HumidityCrafterBlock("mof-water-collector") {
            {
                requirements(Category.crafting, ItemStack.with(
                    Items.copper, 150,
                    Items.lead, 120,
                    Items.titanium, 90,
                    Items.silicon, 80,
                    Items.metaglass, 60,
                    Items.plastanium, 50,
                    Items.surgeAlloy, 30,
                    Items.phaseFabric, 20
                ));
                size = 4;
                health = 500;
                craftTime = 80f;
                hasPower = true;
                hasItems = true;
                hasLiquids = true;
                liquidCapacity = 50f;
                outputLiquid = new LiquidStack(Liquids.water, 3f);
                consumeItem(MOFMaterial.item, 1);
                consumePower(0.8f);
                shownPlanets.add(Planets.erekir);
                shownPlanets.add(Planets.serpulo);
                craftEffect = Fx.vapor;
                updateEffect = Fx.steam;
                updateEffectChance = 0.15f;
                updateEffectSpread = 10f;
                warmupSpeed = 0.01f;
            }
            @Override
            public float getHumidityMultiplier() {
                float humidity = PlanetHumidity.getCurrentHumidity();
                return 0.85f + (humidity / 100f) * 0.25f;
            }
        };
    }
}
