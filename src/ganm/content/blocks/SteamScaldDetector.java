package ganm.content.blocks;
import mindustry.Vars;
import mindustry.type.Category;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import ganm.content.liquids.Steam;
import ganm.content.status.Scalding;
/**
 * 蒸汽烫伤检测器
 * 完全隐形、无碰撞、不可建造的检测方块，游戏加载时自动放置在地图中心。
 * 每10帧全局扫描所有单位，附近有水蒸气的施加烫伤。
 */
public class SteamScaldDetector extends GenericCrafter {
    public SteamScaldDetector(String name) {
        super(name);
        size = 1;
        solid = false;
        update = true;
        hasPower = false;
        hasItems = false;
        hasLiquids = false;
        health = 999999;
        craftTime = 1f;
        requirements(Category.crafting, mindustry.type.ItemStack.with());
    }
    public class Build extends GenericCrafterBuild {
        private int tickCounter = 0;
        @Override
        public void updateTile() {
            tickCounter++;
            if (tickCounter % 10 != 0) return; // 每10帧检测一次
            // 全局扫描所有单位
            for (Unit unit : Groups.unit) {
                if (!unit.isValid()) continue;
                // 检查单位周围3格内有没有存储水蒸气的建筑
                boolean nearSteam = false;
                int ux = (int)(unit.x / 8);
                int uy = (int)(unit.y / 8);
                for (int x = ux - 3; x <= ux + 3; x++) {
                    for (int y = uy - 3; y <= uy + 3; y++) {
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
                    unit.apply(Scalding.effect, 60f); // 持续1秒
                }
            }
        }
    }
}
