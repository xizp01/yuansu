package ganm.world.blocks;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.type.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.ui.Styles;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ButtonGroup;
import arc.graphics.Color;

/**
 * 多配方工厂基类
 * 支持玩家在多个配方之间切换
 */
public class MultiRecipeCrafter extends GenericCrafter {

    /**
     * 配方定义
     */
    public static class Recipe {
        public ItemStack[] items = {};      // 输入物品
        public LiquidStack[] liquids = {};  // 输入液体
        public ItemStack outputItem;        // 输出物品（可选）
        public LiquidStack outputLiquid;    // 输出液体（可选）
        public float craftTime = 60f;       // 制作时间
        public float powerPerCraft = 0f;    // 每次制作消耗的电力

        public Recipe() {}

        public Recipe items(ItemStack... items) {
            this.items = items;
            return this;
        }

        public Recipe liquids(LiquidStack... liquids) {
            this.liquids = liquids;
            return this;
        }

        public Recipe outputItem(ItemStack output) {
            this.outputItem = output;
            return this;
        }

        public Recipe outputLiquid(LiquidStack output) {
            this.outputLiquid = output;
            return this;
        }

        public Recipe craftTime(float time) {
            this.craftTime = time;
            return this;
        }

        public Recipe powerPerCraft(float power) {
            this.powerPerCraft = power;
            return this;
        }
    }

    /** 所有配方 */
    public Seq<Recipe> recipes = new Seq<>();

    public MultiRecipeCrafter(String name) {
        super(name);
        configurable = true;
        clearOnDoubleTap = true;
        hasItems = true;
        hasLiquids = true;
        hasPower = true;

        // 配置：通过索引切换配方
        config(Integer.class, (MultiRecipeBuild build, Integer i) -> {
            if (build.currentRecipe == i) return;
            build.currentRecipe = (i < 0 || i >= recipes.size) ? -1 : i;
            build.progress = 0;
        });
    }

    @Override
    public void init() {
        // 计算物品容量
        itemCapacity = 10;
        for (Recipe recipe : recipes) {
            for (ItemStack stack : recipe.items) {
                itemCapacity = Math.max(itemCapacity, stack.amount * 2);
            }
        }
        super.init();
    }

    public class MultiRecipeBuild extends GenericCrafterBuild {
        /** 当前选中的配方索引 */
        public int currentRecipe = -1;

        @Override
        public void created() {
            // 自动选择第一个配方
            if (currentRecipe == -1 && recipes.size > 0) {
                currentRecipe = 0;
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            if (recipes.size <= 1) return;

            // 简单的配方选择按钮
            table.row();
            Table buttons = new Table();
            int columns = 2;
            int i = 0;
            ButtonGroup<ImageButton> group = new ButtonGroup<>();
            group.setMinCheckCount(0);

            for (int r = 0; r < recipes.size; r++) {
                int idx = r;
                Recipe recipe = recipes.get(r);

                // 按钮显示输出物品/液体的图标
                if (recipe.outputItem != null) {
                    ImageButton button = buttons.button(recipe.outputItem.item.uiIcon, Styles.clearNoneTogglei, 40f, () -> {
                        configure(idx);
                    }).tooltip(recipe.outputItem.item.localizedName).group(group).get();
                    button.update(() -> button.setChecked(currentRecipe == idx));
                } else if (recipe.outputLiquid != null) {
                    ImageButton button = buttons.button(recipe.outputLiquid.liquid.fullIcon, Styles.clearNoneTogglei, 40f, () -> {
                        configure(idx);
                    }).tooltip(recipe.outputLiquid.liquid.localizedName).group(group).get();
                    button.update(() -> button.setChecked(currentRecipe == idx));
                }

                if (++i % columns == 0) {
                    buttons.row();
                }
            }
            table.add(buttons).fillX().left();
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            if (recipes.size > 1 && currentRecipe != -1 && currentRecipe < recipes.size) {
                Recipe recipe = recipes.get(currentRecipe);
                if (recipe.outputItem != null) {
                    drawItemSelection(recipe.outputItem.item);
                } else if (recipe.outputLiquid != null) {
                    drawItemSelection(recipe.outputLiquid.liquid);
                }
            }
        }

        @Override
        public void display(Table table) {
            super.display(table);
            table.row();
            table.table(t -> {
                t.left();
                if (currentRecipe != -1 && currentRecipe < recipes.size) {
                    Recipe recipe = recipes.get(currentRecipe);
                    if (recipe.outputItem != null) {
                        t.image(recipe.outputItem.item.uiIcon).size(32).padBottom(-4).padRight(2).scaling(arc.scene.style.Scaling.fit);
                        t.label(recipe.outputItem.item.localizedName).wrap().width(230f).color(Color.lightGray);
                    } else if (recipe.outputLiquid != null) {
                        t.image(recipe.outputLiquid.liquid.fullIcon).size(32).padBottom(-4).padRight(2).scaling(arc.scene.style.Scaling.fit);
                        t.label(recipe.outputLiquid.liquid.localizedName).wrap().width(230f).color(Color.lightGray);
                    }
                }
            }).left();
        }

        @Override
        public Object config() {
            return currentRecipe;
        }

        @Override
        public boolean shouldConsume() {
            if (currentRecipe == -1) return false;
            Recipe recipe = recipes.get(currentRecipe);

            // 检查输出是否还有空间
            if (recipe.outputItem != null) {
                if (items.get(recipe.outputItem.item) + recipe.outputItem.amount > itemCapacity) {
                    return false;
                }
            }
            if (recipe.outputLiquid != null && !ignoreLiquidFullness) {
                if (liquids.get(recipe.outputLiquid.liquid) >= liquidCapacity - 0.001f) {
                    return false;
                }
            }

            return enabled;
        }

        @Override
        public void updateTile() {
            if (currentRecipe < 0 || currentRecipe >= recipes.size) {
                currentRecipe = -1;
            }

            if (efficiency > 0 && currentRecipe != -1) {
                Recipe recipe = recipes.get(currentRecipe);

                // 更新进度
                progress += getProgressIncrease(recipe.craftTime);
                warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);

                // 连续输出液体
                if (recipe.outputLiquid != null) {
                    float inc = getProgressIncrease(1f);
                    handleLiquid(this, recipe.outputLiquid.liquid,
                        Math.min(recipe.outputLiquid.amount * inc, liquidCapacity - liquids.get(recipe.outputLiquid.liquid)));
                }

                // 更新效果
                if (wasVisible && Mathf.chanceDelta(updateEffectChance)) {
                    updateEffect.at(x + Mathf.range(size * updateEffectSpread), y + Mathf.range(size * updateEffectSpread));
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed);
            }

            totalProgress += warmup * Time.delta;

            // 完成制作
            if (progress >= 1f) {
                consume();
                if (recipe.outputItem != null) {
                    offload(recipe.outputItem.item);
                }
                if (wasVisible) {
                    craftEffect.at(x, y);
                }
                progress %= 1f;
            }

            // 输出物品/液体
            dumpOutputs();
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

    @Override
    public MultiRecipeBuild newBuilding() {
        return new MultiRecipeBuild();
    }
}
