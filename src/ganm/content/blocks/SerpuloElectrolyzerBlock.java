package ganm.content.blocks;

import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import ganm.content.liquids.Oxygen;

/**
 * 塞普罗电解制氢机（自定义方块类）
 * 电解水同时产出氢气和氧气（比例 2:1）。
 * 氢气从左右输出，氧气从上下输出，避免管道冲突。
 */
public class SerpuloElectrolyzerBlock extends GenericCrafter {
    public float hydrogenOutput = 5f;
    public float oxygenOutput = 2.5f;

    public SerpuloElectrolyzerBlock(String name) {
        super(name);
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
        // 不使用默认outputLiquid，完全自定义输出
        outputLiquid = null;
        consumeLiquid(Liquids.water, 10f);
        consumePower(2.0f);
        shownPlanets.add(Planets.serpulo);
        craftEffect = Fx.vapor;
        updateEffect = Fx.steam;
        updateEffectChance = 0.1f;
        updateEffectSpread = 6f;
        warmupSpeed = 0.03f;
    }

    public class SerpuloElectrolyzerBuild extends GenericCrafterBuild {
        @Override
        public void craft() {
            // 制作完成时同时添加氢气和氧气到液体容器
            liquids.add(Liquids.hydrogen, hydrogenOutput);
            liquids.add(Oxygen.liquid, oxygenOutput);
        }

        @Override
        public void update() {
            super.update();
            // 氢气从左右方向输出（0=右, 2=左）
            dumpLiquid(Liquids.hydrogen, 0);
            dumpLiquid(Liquids.hydrogen, 2);
            // 氧气从上下方向输出（1=下, 3=上）
            dumpLiquid(Oxygen.liquid, 1);
            dumpLiquid(Oxygen.liquid, 3);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            // 只接受水作为输入
            return liquid == Liquids.water;
        }
    }
}
