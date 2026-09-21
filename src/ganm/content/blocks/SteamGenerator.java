package ganm.content.blocks;

import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumePower;
import mindustry.content.*;
import mindustry.gen.Building;
import ganm.content.liquids.Steam;

/**
 * 蒸汽发生器（工业蒸汽锅炉，多配方）
 * 直接继承 GenericCrafter，Build 为其非 static 内部类（v7 要求）。
 * 两种加热模式，点击面板切换：
 *   配方0 燃料加热：水+煤 -> 蒸汽（产量高，不耗电极）
 *   配方1 电加热：  水+电 -> 蒸汽（产量略低，清洁）
 */
public class SteamGenerator extends GenericCrafter {

    public static Block block;

    public SteamGenerator(String name) {
        super(name);
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

        // 动态电力：基础 2.0kW；燃料加热配方（索引0）时不请求电力。
        // v7 无 enabled 字段，靠匿名子类重写 requestedPower 实现按配方切换耗电。
        consume(new ConsumePower(2.0f, 0f, false) {
            @Override
            public float requestedPower(Building entity) {
                if (entity instanceof SteamGenBuild sgb && sgb.currentRecipe == 0) return 0f;
                return usage;
            }
        });

        buildType = SteamGenBuild::new;

        // 配方切换回调（联机安全：UI 调 configure -> 服务端执行此 lambda）
        config(Integer.class, (SteamGenBuild build, Integer i) -> {
            build.currentRecipe = Math.max(0, Math.min(1, i));
            build.prog = 0f;
        });
    }

    public static void register() {
        block = new SteamGenerator("steam-generator");
    }

    /** 自定义建筑：完全手写合成逻辑。必须是外部 Block 的非 static 内部类。 */
    public class SteamGenBuild extends GenericCrafterBuild {
        // 配方参数（按索引）
        final float[] waterAmt  = {10f, 10f}; // 每周期耗水
        final int[]   coalAmt   = {2, 0};    // 每周期耗煤
        final float[] steamOut  = {10f, 8f}; // 每周期产蒸汽
        final float[] craftTime = {50f, 60f};// 每周期 tick

        /** 当前配方索引（联机由 configure 下发，存档持久化） */
        public int currentRecipe = 0;
        /** 手写进度 0 ~ craftTime[currentRecipe] */
        public float prog = 0f;

        @Override
        public void updateTile() {
            int r = currentRecipe;

            boolean canRun = true;
            if (liquids.get(Liquids.water) < waterAmt[r]) canRun = false;
            if (coalAmt[r] > 0 && items.get(Items.coal) < coalAmt[r]) canRun = false;
            if (liquids.get(Steam.liquid) >= liquidCapacity - 0.001f) canRun = false;
            // 仅电加热配方要求电力（efficiency 由 ConsumePower 维护）
            if (r == 1 && efficiency <= 0.01f) canRun = false;

            if (!canRun) {
                prog = 0f;
                return;
            }

            prog += delta();
            if (prog >= craftTime[r]) {
                prog = 0f;
                liquids.remove(Liquids.water, waterAmt[r]);
                if (coalAmt[r] > 0) items.remove(Items.coal, coalAmt[r]);
                handleLiquid(this, Steam.liquid, steamOut[r]);
            }

            // 持续把蒸汽排到相邻管道
            dumpLiquid(Steam.liquid);
        }

        @Override
        public void buildConfiguration(Table table) {
            table.row();
            for (int i = 0; i < 2; i++) {
                int idx = i;
                table.button(i == 0 ? "燃料加热（煤）" : "电加热", () -> configure(idx))
                        .checked(currentRecipe == idx)
                        .size(150, 40).pad(4);
                table.row();
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(currentRecipe);
            write.f(prog);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            currentRecipe = read.i();
            prog = read.f();
        }
    }
}
