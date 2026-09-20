package ganm.content.blocks;

import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Oxygen;

/**
 * 塞普罗电解制氢机
 * 电解水同时产出氢气和氧气（比例 2:1），为塞普罗同位素分离产业链提供原料。
 * 所属星球：塞普罗
 */
public class SerpuloElectrolyzer {
    public static Block block;

    public static void load() {
        block = new GenericCrafter("serpulo-electrolyzer") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 40,
                Items.lead, 30,
                Items.titanium, 15,
                Items.silicon, 20
            ));
            size = 2;
            health = 200;
            craftTime = 60f;
            hasPower = true;
            hasLiquids = true;
            liquidCapacity = 30f;
            // 双液体输出：氢气 + 氧气（比例 2:1），和原版电解机一样的机制
            outputLiquids = new LiquidStack[]{
                new LiquidStack(Liquids.hydrogen, 5f),
                new LiquidStack(Oxygen.liquid, 2.5f)
            };
            // 分方向输出：氢气从右(0)，氧气从左(2)，旋转方块时方向跟随旋转
            liquidOutputDirections = new int[]{0, 2};
            consumeLiquid(Liquids.water, 10f);
            consumePower(2.0f);
            shownPlanets.add(Planets.serpulo);
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.1f;
            updateEffectSpread = 6f;
            warmupSpeed = 0.03f;
        }};
    }
}
