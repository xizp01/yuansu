package ganm.content.blocks;

import arc.func.Prov;
import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.content.*;
import ganm.content.liquids.Steam;

/**
 * 蒸汽发生器（工业蒸汽锅炉，多配方）
 * 支持两种加热方式：
 * 配方0：燃料加热（水+煤 → 水蒸气，产量高，不需要电）
 * 配方1：电加热（水+电力 → 水蒸气，产量低，清洁方便）
 * 所属星球：埃里克尔、塞普罗通用
 */
public class SteamGenerator {
    public static Block block;

    /**
     * 配方记录类
     */
    public static class SteamRecipe {
        public float waterAmount;    // 消耗水
        public int coalAmount;       // 消耗煤（物品）
        public float powerConsume;   // 每秒耗电
        public float steamOut;       // 产出蒸汽
        public float craftTime;      // 合成总tick

        public SteamRecipe(float waterAmount, int coalAmount, float powerConsume, float steamOut, float craftTime) {
            this.waterAmount = waterAmount;
            this.coalAmount = coalAmount;
            this.powerConsume = powerConsume;
            this.steamOut = steamOut;
            this.craftTime = craftTime;
        }
    }

    /**
     * 自定义 Build 类
     */
    public static class SteamGenBuild extends GenericCrafterBuild {
        /** 所有配方 */
        public static final SteamRecipe[] recipes = {
            // 配方0：燃料加热（水+煤 → 水蒸气，产量高，不需要电）
            new SteamRecipe(10f, 2, 0f, 10f, 50f),
            // 配方1：电加热（水+电力 → 水蒸气，产量低，清洁方便）
            new SteamRecipe(10f, 0, 2.0f, 8f, 60f)
        };

        /** 当前选中的配方索引 */
        public int currentRecipe = 0;

        @Override
        public void updateTile() {
            SteamRecipe recipe = recipes[currentRecipe];

            // 设置当前配方的耗电
            if (recipe.powerConsume > 0) {
                power.graph.updateConsumption(recipe.powerConsume);
            }

            // 检查是否可以生产
            boolean canProduce = true;

            // 检查电力
            if (recipe.powerConsume > 0 && power.status.meter.get() <= 0) {
                canProduce = false;
            }

            // 检查水
            if (liquids.get(Liquids.water) < recipe.waterAmount) {
                canProduce = false;
            }

            // 检查煤
            if (recipe.coalAmount > 0 && items.get(Items.coal) < recipe.coalAmount) {
                canProduce = false;
            }

            // 检查输出蒸汽是否还有空间
            if (liquids.get(Steam.liquid) >= liquidCapacity - 0.001f) {
                canProduce = false;
            }

            if (canProduce) {
                // 进度增加
                progress += edelta() / recipe.craftTime;
                warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);

                // 更新效果
                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }

                // 进度满了，完成制作
                if (progress >= 1f) {
                    progress %= 1f;

                    // 消耗输入
                    liquids.remove(Liquids.water, recipe.waterAmount);
                    if (recipe.coalAmount > 0) {
                        items.remove(Items.coal, recipe.coalAmount);
                    }
                    if (recipe.powerConsume > 0) {
                        power.remove(recipe.powerConsume * recipe.craftTime / 60f);
                    }

                    // 产出蒸汽
                    handleLiquid(this, Steam.liquid, Math.min(recipe.steamOut, liquidCapacity - liquids.get(Steam.liquid)));

                    // 制作效果
                    if (wasVisible) {
                        craftEffect.at(x, y);
                    }
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            // 输出蒸汽
            dumpLiquid(Steam.liquid, 2f, -1);
        }

        @Override
        public void buildConfiguration(Table table) {
            table.row();
            table.button("切换配方（当前: " + (currentRecipe == 0 ? "燃料加热" : "电加热") + "）", () -> {
                configure((currentRecipe + 1) % recipes.length);
            }).size(150f, 40f);
        }

        @Override
        public void configure(Object value) {
            if (value instanceof Integer) {
                currentRecipe = (Integer) value;
                progress = 0f;
            }
        }

        @Override
        public void display(Table table) {
            super.display(table);
            table.row();
            table.label(() -> "当前配方: " + (currentRecipe == 0 ? "燃料加热" : "电加热")).left();
        }

        @Override
        public void write(Reads read, Writes write) {
            super.write(read, write);
            write.i(currentRecipe);
        }

        @Override
        public void read(Reads read, Writes write) {
            super.read(read, write);
            currentRecipe = read.i();
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
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            configurable = true;
            liquidCapacity = 40f;
            itemCapacity = 20;

            // 工业锅炉效果：火焰+蒸汽
            craftEffect = Fx.vapor;
            updateEffect = Fx.steam;
            updateEffectChance = 0.15f;
            updateEffectSpread = 8f;
            warmupSpeed = 0.04f;

            // 双星球通用
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);

            // 设置自定义 Building 类
            buildType = (Prov<Building>) SteamGenBuild::new;

            // 配置：通过索引切换配方
            config(Integer.class, (SteamGenBuild build, Integer i) -> {
                build.currentRecipe = i;
                build.progress = 0f;
            });
        }};
    }
}
