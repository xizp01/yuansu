package ganm.content.blocks;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.Items;
import mindustry.content.Planets;
import mindustry.content.Fx;
import ganm.content.items.SilicaGel;
/**
 * 硅胶合成机
 * 利用二氧化硅（沙子）制造多孔硅胶干燥剂，为除湿转轮取水器提供耗材。
 * 所属星球：埃里克尔、塞普罗
 */
public class SilicaGelSynthesizer {
    public static Block block;
    public static void load() {
        block = new GenericCrafter("silica-gel-synthesizer") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 40,
                Items.lead, 30,
                Items.titanium, 20,
                Items.silicon, 25,
                Items.metaglass, 15
            ));
            size = 2;
            health = 200;
            craftTime = 90f;
            hasPower = true;
            hasItems = true;
            outputItem = new ItemStack(SilicaGel.item, 3);
            consumeItem(Items.sand, 8);
            consumePower(1.5f);
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.05f;
            warmupSpeed = 0.02f;
        }};
    }
}
