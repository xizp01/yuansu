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
import ganm.content.items.LithiumChloride;
/**
 * 三代：氯化锂复合吸附集水器
 * 盐-多孔载体复合吸附剂，双床间歇循环，极低湿度仍能捕获水汽。
 * 消耗氯化锂干燥剂，湿度>15即可满负荷，干旱星球主力供水设备。
 * 所属星球：埃里克尔、塞普罗
 */
public class LithiumChlorideCollector {
    public static Block block;
    public static void load() {
        block = new HumidityCrafterBlock("lithium-chloride-collector") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 100,
                Items.lead, 80,
                Items.titanium, 60,
                Items.silicon, 55,
                Items.metaglass, 40,
                Items.plastanium, 30,
                Items.surgeAlloy, 15
            ));
            size = 3;
            health = 380;
            craftTime = 80f;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            liquidCapacity = 35f;
            outputLiquid = new LiquidStack(Liquids.water, 2.5f);
            consumeItem(LithiumChloride.item, 1);
            consumePower(2.5f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.12f;
            updateEffectSpread = 8f;
            warmupSpeed = 0.012f;
        }} {
            @Override
            public float getHumidityMultiplier() {
                float humidity = PlanetHumidity.getCurrentHumidity();
                return (humidity > 15f) ? 1.0f : 0.3f;
            }
        };
    }
}
