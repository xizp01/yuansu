package ganm.content;

import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.Fx;

public class YuansuBlocks {
    public static Block protiumSeparator, deuteriumSeparator, tritiumSeparator;

    public static void load() {
        protiumSeparator = new GenericCrafter("protium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 30,
                Items.lead, 20,
                Items.titanium, 10,
                Items.silicon, 10
            ));
            size = 2;
            health = 160;
            craftTime = 120f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            outputLiquid = new LiquidStack(YuansuLiquids.protium, 3f);
            consumeLiquid(Liquids.hydrogen, 5f);
            consumePower(1.0f);
            shownPlanets.add(Planets.erekir);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.06f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.02f;
        }};

        deuteriumSeparator = new GenericCrafter("deuterium-separator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 40,
                Items.lead, 30,
                Items.titanium, 15,
                Items.silicon, 15,
                Items.metaglass, 10
            ));
            size = 2;
            health = 200;
            craftTime = 180f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            outputLiquid = new LiquidStack(YuansuLiquids.deuterium, 1.5f);
            consumeLiquid(Liquids.hydrogen, 6f);
            consumePower(1.5f);
            shownPlanets.add(Planets.erekir);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.05f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.015f;
        }};

        tritiumSeparator = new GenericCrafter("tritium-separator") {{
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
            craftTime = 240f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 12f;
            outputLiquid = new LiquidStack(YuansuLiquids.tritium, 0.5f);
            consumeLiquid(Liquids.hydrogen, 8f);
            consumePower(2.0f);
            shownPlanets.add(Planets.erekir);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.04f;
            updateEffectSpread = 5f;
            warmupSpeed = 0.01f;
        }};
    }
}
