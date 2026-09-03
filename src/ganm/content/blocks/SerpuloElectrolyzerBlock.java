package ganm.content.blocks;

import arc.math.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.content.*;
import ganm.content.liquids.Oxygen;

/**
 * 塞普罗电解制氢机（完全自定义方块）
 * 电解水同时产出氢气和氧气（比例 2:1）。
 * 氢气从左右输出，氧气从上下输出。
 */
public class SerpuloElectrolyzerBlock extends Block {
    public float craftTime = 60f;
    public float hydrogenOutput = 5f;
    public float oxygenOutput = 2.5f;
    public Effect craftEffect = Fx.vapor;
    public Effect updateEffect = Fx.steam;
    public float updateEffectChance = 0.1f;

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
        hasPower = true;
        hasLiquids = true;
        liquidCapacity = 30f;
        // 消耗配置
        consumeLiquid(Liquids.water, 10f);
        consumePower(2.0f);
        shownPlanets.add(Planets.serpulo);
    }

    public class SerpuloElectrolyzerBuild extends Building {
        public float progress;
        public float warmup;

        @Override
        public void update() {
            super.update();
            warmup = Mathf.lerpDelta(warmup, efficiency > 0 ? 1f : 0f, 0.05f);

            if (efficiency > 0) {
                progress += 1f / craftTime * efficiency;
                if (Mathf.random() < updateEffectChance * efficiency) {
                    updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4f));
                }
                if (progress >= 1f) {
                    progress = 0f;
                    craftEffect.at(x, y);
                    liquids.add(Liquids.hydrogen, hydrogenOutput);
                    liquids.add(Oxygen.liquid, oxygenOutput);
                }
            }

            // 氢气从左右输出（0=右, 2=左）
            pushLiquid(Liquids.hydrogen, 0);
            pushLiquid(Liquids.hydrogen, 2);
            // 氧气从上下输出（1=下, 3=上）
            pushLiquid(Oxygen.liquid, 1);
            pushLiquid(Oxygen.liquid, 3);
        }

        private void pushLiquid(Liquid liquid, int direction) {
            float amount = liquids.get(liquid);
            if (amount <= 0) return;
            Building next = nearby(direction);
            if (next != null && next.block.hasLiquids && next.acceptLiquid(this, liquid)) {
                float flow = Math.min(amount, 10f * edelta());
                next.handleLiquid(this, liquid, flow);
                liquids.remove(liquid, flow);
            }
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid) {
            return liquid == Liquids.water;
        }

        @Override
        public float warmup() {
            return warmup;
        }

        @Override
        public float progress() {
            return progress;
        }

        @Override
        public boolean shouldConsume() {
            return enabled;
        }
    }
}
