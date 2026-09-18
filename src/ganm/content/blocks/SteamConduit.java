package ganm.content.blocks;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import mindustry.type.Category;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.content.Items;
/**
 * 蒸汽管道
 * 专门运输高温水蒸气的管道，管道内的水蒸气不会造成烫伤。
 */
public class SteamConduit extends Conduit {
    public SteamConduit(String name) {
        super(name);
        requirements(Category.liquid, mindustry.type.ItemStack.with(
            Items.copper, 1,
            Items.metaglass, 1
        ));
        botColor = Color.valueOf("8b4513"); // 棕红色，体现高温蒸汽
        leaks = false; // 像电镀导管一样不泄漏
    }
    @Override
    public void load() {
        super.load();
        // 从一张大图切割出5个top贴图（参考NewHorizonMod的SpriteUtil.splitRegionArray）
        TextureRegion big = Core.atlas.find(name + "-top");
        if (big.found() && big.texture != null) {
            int tileWidth = 32;
            int tileHeight = 32;
            int pad = 1;
            int pWidth = tileWidth + pad * 2;
            int pHeight = tileHeight + pad * 2;
            int x = big.getX();
            int y = big.getY();
            int width = big.width;
            int height = big.height;
            int sw = width / pWidth;
            int sh = height / pHeight;
            int startX = x;
            topRegions = new TextureRegion[sw * sh];
            for (int cy = 0; cy < sh; cy++, y += pHeight) {
                x = startX;
                for (int cx = 0; cx < sw; cx++, x += pWidth) {
                    int index = cx + cy * sw;
                    topRegions[index] = new TextureRegion(big.texture, x + pad, y + pad, tileWidth, tileHeight);
                }
            }
        }
    }
}
