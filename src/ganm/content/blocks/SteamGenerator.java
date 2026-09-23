package ganm.content.blocks;

import arc.scene.ui.layout.Table;
import arc.util.Time;
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
        update = true;
        hasLiquids = true;
        hasItems = true;
        hasPower = true;
        outputsLiquid = true;
        configurable = true;
        liquidCapacity = 40f;
        itemCapacity = 20;

        // 注册水输入（让方块接受水管输入）；煤通过 consumeItems 注册接受
        consumeLiquid(Liquids.water, 10f);
        consumeItems(ItemStack.with(Items.coal, 2));

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

            // 排蒸汽必须在 canRun 检查之前：否则蒸汽一满就 return，永远排不出去，死锁
            dumpLiquid(Steam.liquid);

            boolean canRun = true;
            if (liquids.get(Liquids.water) < waterAmt[r]) canRun = false;
            if (coalAmt[r] > 0 && items.get(Items.coal) < coalAmt[r]) canRun = false;
            if (liquids.get(Steam.liquid) >= liquidCapacity - 0.001f) canRun = false;
            // 仅电加热配方要求电力；注册了 consumeItems(coal) 后 efficiency 会受煤影响，
            // 所以这里直接读电网状态 power.status，避免煤不够时电加热也被卡住。
            if (r == 1 && (power == null || power.status <= 0.01f)) canRun = false;

            if (!canRun) {
                return; // 资源不足时暂停，保留已有进度（切换配方时才重置）
            }

            prog += Time.delta;
            if (prog >= craftTime[r]) {
                prog = 0f;
                liquids.remove(Liquids.water, waterAmt[r]);
                if (coalAmt[r] > 0) items.remove(Items.coal, coalAmt[r]);
                handleLiquid(this, Steam.liquid, steamOut[r]);
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            table.row();
            // 展开两个配方选项；点击后选中并高亮，然后整体关闭配置面板（隐藏）。
            // 再次点击机器会重新调用本方法，.checked(currentRecipe == idx) 使上次选中的按钮保持高亮。
            for (int i = 0; i < 2; i++) {
                int idx = i;
                table.button(i == 0 ? "燃料加热（煤）" : "电加热", () -> {
                            // 本地立即生效（兜底），同时走 configure 保证联机同步
                            currentRecipe = idx;
                            prog = 0f;
                            configure(idx);
                            // 关闭配置面板（隐藏整个选择页）；再次点击机器重新打开并高亮上次选择
                            deselect();
                        })
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
