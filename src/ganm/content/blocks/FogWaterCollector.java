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
 * 雾水收集网
 * 被动式集水设备，通过细密网格拦截空气中的雾滴汇聚成水。
 * 零耗电，产量较低，适合作为偏远据点辅助水源。
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
        }};
    }
}
