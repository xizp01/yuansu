package ganm.content;

import arc.graphics.*;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.Fx;

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

            // 运行动画：蒸汽特效
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.08f;
            updateEffectSpread = 6f;
            warmupSpeed = 0.025f;

            // 绘制：默认本体 + 中心脉冲发光（氕气颜色）
            drawer = new DrawMulti(
                new DrawDefault(),
                new DrawPulse() {{
                    color = Color.valueOf("a8b8f5");
                    radius = 2.5f;
                    stroke = 1.2f;
                    timeScl = 55f;
                }}
            );
        }};
    }
}
