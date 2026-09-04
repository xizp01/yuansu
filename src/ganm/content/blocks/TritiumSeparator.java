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
import ganm.content.liquids.Tritium;
/**
 * 氚气分离机
 * 通过高精度同位素分离工艺，从氢气中提取微量氚气，需要高级材料。
 * 所属星球：埃里克尔、塞普罗
 */
public class TritiumSeparator {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("tritium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 50,
                Items.lead, 40,
                Items.titanium, 25,
                Items.silicon, 20,
                Items.metaglass, 15,
                Items.surgeAlloy, 10
            ));
            size = 2;
            health = 240;
            craftTime = 360f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 30f;
            outputLiquid = new LiquidStack(Tritium.liquid, 0.5f);
            consumeLiquid(Liquids.hydrogen, 30f);
            consumePower(2.0f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.04f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.01f;
        }};
    }
}
