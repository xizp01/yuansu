package ganm.content;

import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;

public class YuansuBlocks {
    public static Block protiumSeparator;

    public static void load() {
        protiumSeparator = new GenericCrafter("protium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 50,
                Items.lead, 30,
                Items.titanium, 20,
                Items.silicon, 15
            ));
            size = 2;
            health = 200;
            craftTime = 60f;
            hasPower = true;
            hasLiquids = true;
            outputLiquid = new LiquidStack(YuansuLiquids.protium, 8f);
            consumeLiquid(Liquids.hydrogen, 10f);
            consumePower(1.5f);
            // 限定在埃里克尔星球显示
            shownPlanets.add(Planets.erekir);
        }};
    }
}
