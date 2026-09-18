package ganm.content.blocks;
import mindustry.Vars;
import mindustry.type.Category;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.content.Items;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import ganm.content.liquids.Steam;
import ganm.content.status.Scalding;
/**
 * 蒸汽管道
 * 专门运输高温水蒸气的管道，也可以运输其他液体。
 * 管道本身带有蒸汽烫伤检测：站在管道附近的单位会被烫伤。
 */
public class SteamConduit extends Conduit {
    public SteamConduit(String name) {
        super(name);
        requirements(Category.liquid, mindustry.type.ItemStack.with(
            Items.copper, 1,
            Items.metaglass, 1
        ));
        // 可以运输所有液体，包括水蒸气
        // 水蒸气只能在这个管道里运输
    }
    public class Build extends ConduitBuild {
        private int tickCounter = 0;
        @Override
        public void update() {
            super.update();
            // 蒸汽烫伤检测：每10帧执行一次
            tickCounter++;
            if (tickCounter % 10 != 0) return;
            // 只有管道里有水蒸气时才检测
            if (liquids == null || liquids.currentAmount() <= 0) return;
            if (liquids.current() != Steam.liquid) return;
            // 扫描周围单位，施加烫伤
            for (Unit unit : Groups.unit) {
                if (!unit.isValid()) continue;
                float dist = this.dst(unit);
                if (dist < 16f) { // 2格半径
                    unit.apply(Scalding.effect, 60f); // 持续1秒
                }
            }
        }
    }
}
