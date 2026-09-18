package ganm.content.blocks;
import arc.graphics.Color;
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
 * 同时承担全局蒸汽烫伤检测的职责。
 */
public class SteamConduit extends Conduit {
    public SteamConduit(String name) {
        super(name);
        requirements(Category.liquid, mindustry.type.ItemStack.with(
            Items.copper, 1,
            Items.metaglass, 1
        ));
        botColor = Color.valueOf("8b4513"); // 棕红色，体现高温蒸汽
    }
    public class Build extends ConduitBuild {
        private int tickCounter = 0;
        @Override
        public void updateTile() {
            super.updateTile();
            // 全局蒸汽烫伤检测：每10帧执行一次
            tickCounter++;
            if (tickCounter % 10 != 0) return;
            // 全局扫描所有单位
            for (Unit unit : Groups.unit) {
                if (!unit.isValid()) continue;
                // 检查单位周围3格内有没有存储水蒸气的建筑
                boolean nearSteam = false;
                int ux = (int)(unit.x / 8);
                int uy = (int)(unit.y / 8);
                for (int x = ux - 3; x <= ux + 3; x++) {
                    for (int y = uy - 3; y <= uy + 3; y++) {
                        var tile = mindustry.Vars.world.tile(x, y);
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
                    unit.apply(Scalding.effect, 60f); // 持续1秒
                }
            }
        }
    }
}
