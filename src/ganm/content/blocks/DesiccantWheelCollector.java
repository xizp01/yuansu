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
import ganm.content.items.SilicaGel;
/**
 * 二代：除湿转轮取水器
 * 硅胶转轮连续吸附-热风再生循环，一边吸附水汽一边高温再生，连续产水。
 * 消耗硅胶干燥剂，湿度≥25即可满速工作，适合半干旱星球。
 * 所属星球：埃里克尔、塞普罗
 */
public class DesiccantWheelCollector {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("desiccant-wheel-collector") {
            {
                requirements(Category.crafting, ItemStack.with(
                    Items.copper, 80,
                    Items.lead, 60,
                    Items.titanium, 40,
                    Items.silicon, 45,
                    Items.metaglass, 30,
                    Items.plastanium, 20
                ));
                size = 3;
                health = 320;
                craftTime = 80f;
                hasPower = true;
                hasItems = true;
                hasLiquids = true;
                liquidCapacity = 30f;
                outputLiquid = new LiquidStack(Liquids.water, 2.2f);
                consumeItem(SilicaGel.item, 1);
                consumePower(2.0f);
                shownPlanets.add(Planets.erekir);
                shownPlanets.add(Planets.serpulo);
                craftEffect = Fx.vapor;
                updateEffect = Fx.steam;
                updateEffectChance = 0.1f;
                updateEffectSpread = 8f;
                warmupSpeed = 0.015f;
            }
            @Override
            public float getProgressIncrease(float baseEfficiency) {
                float humidity = PlanetHumidity.getCurrentHumidity();
                float multiplier = (humidity >= 25f) ? 1.0f : 0.4f;
                return super.getProgressIncrease(baseEfficiency) * multiplier;
            }
        };
    }
}
