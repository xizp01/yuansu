package ganm.content.blocks;

import arc.scene.ui.layout.Table;
import arc.util.io.ReadBuffer;
import arc.util.io.WriteBuffer;
import mindustry.type.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.*;
import ganm.content.liquids.Steam;

/**
 * 蒸汽发生器（工业蒸汽锅炉，多配方）
 * 支持两种加热方式，点击方块面板切换：
 *   配方0 燃料加热：消耗水+煤 → 水蒸气（产量高，不需要电力）
 *   配方1 电加热：消耗水+电力 → 水蒸气（产量略低，清洁方便）
 * 所属星球：埃里克尔、塞普罗通用
 */
public class SteamGenerator {
    public static Block block;

    /** 单条配方参数 */
    public static class SteamRecipe {
        public float waterAmount;   // 每个周期消耗水
        public int coalAmount;      // 每个周期消耗煤（物品，整数）
        public float steamOut;      // 每个周期产出蒸汽
        public float craftTime;     // 合成总 tick
        public SteamRecipe(float waterAmount, int coalAmount, float steamOut, float craftTime) {
            this.waterAmount = waterAmount;
            this.coalAmount = coalAmount;
            this.steamOut = steamOut;
            this.craftTime = craftTime;
        }
    }

    /** 自定义建筑：完全手写合成逻辑 */
    public static class SteamGenBuild extends GenericCrafterBuild {
        public static final SteamRecipe[] recipes = {
            // 0 燃料加热：水10 + 煤2 -> 蒸汽10，50 tick，不耗电
            new SteamRecipe(10f, 2, 10f, 50f),
            // 1 电加热：水10 -> 蒸汽8，60 tick，耗电 2.0（由 Block 侧按配方启用）
            new SteamRecipe(10f, 0, 8f, 60f)
        };

        /** 当前选中配方索引（联机由 configure 下发，存档持久化） */
        public int currentRecipe = 0;
        /** 手写合成进度 0 ~ recipes[currentRecipe].craftTime */
        public float progress = 0f;

        @Override
        public void updateTile() {
            SteamRecipe r = recipes[currentRecipe];

            // 资源检查
            boolean canRun = true;
            // 水不足
            if (liquids.get(Liquids.water) < r.waterAmount) canRun = false;
            // 煤不足
            if (r.coalAmount > 0 && items.get(Items.coal) < r.coalAmount) canRun = false;
            // 蒸汽储罐已满
            if (liquids.get(Steam.liquid) >= liquidCapacity - 0.001f) canRun = false;
            // 电加热模式且电力不足（efficiency 由 ConsumePower 维护；燃料模式 consumer 已 disabled，恒为 1）
            if (currentRecipe == 1 && efficiency <= 0.01f) canRun = false;

            if (!canRun) {
                progress = 0f;
                return;
            }

            // 推进进度（delta() 自动处理游戏倍速）
            progress += delta();

            if (progress >= r.craftTime) {
                progress = 0f;

                // 消耗输入
                liquids.remove(Liquids.water, r.waterAmount);
                if (r.coalAmount > 0) items.remove(Items.coal, r.coalAmount);

                // 产出蒸汽（handleLiquid 自动按容量钳位）
                handleLiquid(this, Steam.liquid, r.steamOut);
            }

            // 持续把蒸汽输出到相邻管道
            dumpLiquid(Steam.liquid);
        }

        /** 面板：两个配方切换按钮，走 configure 实现联机同步 */
        @Override
        public void buildConfiguration(Table table) {
            table.row();
            for (int i = 0; i < recipes.length; i++) {
                int idx = i;
                table.button(i == 0 ? "燃料加热（煤）" : "电加热", () -> configure(idx))
                        .checked(currentRecipe == idx)
                        .size(150, 40).pad(4);
                table.row();
            }
        }

        /** 接收服务端下发的配方索引 */
        @Override
        public void configured(Object value) {
            if (value instanceof Integer) {
                int i = (Integer) value;
                if (i >= 0 && i < recipes.length) {
                    currentRecipe = i;
                    progress = 0f;
                }
            }
        }

        @Override
        public void write(WriteBuffer buffer) {
            super.write(buffer);
            buffer.i(currentRecipe);
            buffer.f(progress);
        }

        @Override
        public void read(ReadBuffer buffer) {
            super.read(buffer);
            currentRecipe = buffer.i();
            progress = buffer.f();
        }
    }

    public static void load() {
        block = new GenericCrafter("steam-generator") {{
            requirements(Category.crafting, ItemStack.with(
                Items.copper, 60,
                Items.lead, 40,
                Items.titanium, 25,
                Items.silicon, 30,
                Items.metaglass, 15
            ));
            size = 2;
            health = 250;
            hasLiquids = true;
            hasItems = true;
            hasPower = true;
            configurable = true;
            liquidCapacity = 40f;
            itemCapacity = 20;

            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);

            // 电力：注册一个 2.0kW 耗电，但仅在选中电加热配方（索引1）时启用；
            // 燃料加热配方时该 consumer 被禁用，方块不耗电极不依赖电网。
            var power = Consume.power(2.0f);
            consume(power);
            power.enabled = b -> ((SteamGenBuild) b).currentRecipe == 1;

            // 绑定自定义 Building
            buildType = SteamGenBuild::new;
        }};
    }
}
