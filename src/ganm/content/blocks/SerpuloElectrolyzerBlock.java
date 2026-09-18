package ganm.content.blocks;
import mindustry.Vars;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.content.*;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import ganm.content.liquids.Oxygen;
import ganm.content.liquids.Steam;
import ganm.content.status.Scalding;
/**
 * 塞普罗电解制氢机
 * 电解水同时产出氢气和氧气（比例 2:1），使用 GenericCrafter 的 outputLiquids 实现双输出。
 * 同时承担全局水蒸气烫伤检测的职责。
 */
public class SerpuloElectrolyzerBlock extends GenericCrafter {
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
    }
    public class Build extends GenericCrafterBuild {
        private int tickCounter = 0;
        @Override
        public void update() {
            super.update();
            // 全局水蒸气烫伤检测：每10帧执行一次
            tickCounter++;
            if (tickCounter % 10 != 0) return;
            for (Unit unit : Groups.unit) {
                if (!unit.isValid()) continue;
                // 检查单位周围3格内有没有存储水蒸气的建筑
                boolean nearSteam = false;
                for (int x = (int)(unit.x/8 - 3); x <= (int)(unit.x/8 + 3); x++) {
                    for (int y = (int)(unit.y/8 - 3); y <= (int)(unit.y/8 + 3); y++) {
                        var tile = Vars.world.tile(x, y);
                        if (tile == null) continue;
                        var build = tile.build;
                        if (build == null || build.liquids == null) continue;
                        if (build.liquids.currentAmount() <= 0) continue;
                        if (build.liquids.current() == Steam.liquid) {
                            nearSteam = true;
                            break;
                        }
                    }
                    if (nearSteam) break;
                }
                if (nearSteam) {
                    unit.apply(Scalding.effect, 60f);
                }
            }
        }
    }
}
