package ganm.world.blocks;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.type.TileEntity;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.GenericCrafter;

/**
 * 多配方工厂基类
 * 支持玩家在多个配方之间切换，类似单位工厂的多单位选择
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
    public Recipe[] recipes = {};

    public MultiRecipeCrafter(String name) {
        super(name);
    }

    @Override
    public void setBars() {
        super.setBars();
        // 可以在这里添加配方选择条
    }

    public class MultiRecipeCrafterBuild extends GenericCrafterBuild {
        /** 当前选中的配方索引 */
        public int currentRecipe = 0;

        @Override
        public void updateEntity() {
            if (recipes.length == 0) return;

            Recipe recipe = recipes[Math.min(currentRecipe, recipes.length - 1)];

            // 检查电力
            if (recipe.powerPerCraft > 0 && power < recipe.powerPerCraft / 60f) {
                progress = 0f;
                warmup = 0f;
                return;
            }

            // 检查输入物品
            for (ItemStack item : recipe.items) {
                if (items.get(item.item) < item.amount) {
                    progress = 0f;
                    warmup = 0f;
                    return;
                }
            }

            // 检查输入液体
            for (LiquidStack liquid : recipe.liquids) {
                if (liquids.get(liquid.liquid) < liquid.amount) {
                    progress = 0f;
                    warmup = 0f;
                    return;
                }
            }

            // 消耗输入
            for (ItemStack item : recipe.items) {
                items.remove(item.item, item.amount);
            }
            for (LiquidStack liquid : recipe.liquids) {
                liquids.remove(liquid.liquid, liquid.amount);
            }
            if (recipe.powerPerCraft > 0) {
                power.remove(recipe.powerPerCraft / 60f);
            }

            // 产出输出
            if (recipe.outputItem != null) {
                offload(recipe.outputItem.item);
            }
            if (recipe.outputLiquid != null) {
                handleLiquid(recipe.outputLiquid.liquid, recipe.outputLiquid.amount);
            }

            // 更新进度
            progress += 1f / recipe.craftTime;
            warmup = Math.min(1f, warmup + 0.02f);

            // 完成制作
            if (progress >= 1f) {
                progress %= 1f;
                consume();
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            if (recipes.length <= 1) return;

            table.button(icon, () -> {
                configure((currentRecipe + 1) % recipes.length);
            }).size(40f).tooltip("切换配方");
        }

        @Override
        public void configured(Object value) {
            if (value instanceof Integer) {
                currentRecipe = (Integer) value;
            }
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
    public TileEntity newBuilding() {
        return new MultiRecipeCrafterBuild();
    }
}
