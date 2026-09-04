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
/**
 * 一代：空气冷凝取水器
 * 压缩制冷路线，模仿除湿机/空调原理，蒸发器表面降温使水汽凝结。
 * 耗电量高，干燥星球效率暴跌，适合潮湿星球。
 * 所属星球：埃里克尔、塞普罗
 */
public class CondensationWaterCollector {
    public static Block block;
    public static void load() {
        block = new HumidityCrafterBlock("condensation-water-collector") {
            {
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
            }
            @Override
            public float getHumidityMultiplier() {
                float humidity = PlanetHumidity.getCurrentHumidity();
                if (humidity >= 50f) return 1.0f;
                if (humidity >= 35f) return 0.5f;
                return 0.2f;
            }
        };
    }
}
