package ganm.content.blocks;
import arc.graphics.Color;
import mindustry.type.Category;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.content.Items;
/**
 * 蒸汽管道
 * 专门运输高温水蒸气的管道，也可以运输其他液体。
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
}
