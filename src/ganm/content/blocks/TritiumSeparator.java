package ganm.content.blocks;

import mindustry.world.Block;

/**
 * 氚气分离机
 * 通过高精度同位素分离工艺，从氢气中提取微量氚气，需要高级材料。
 * 运行时周围会产生辐射区域，对范围内单位造成辐射伤害。
 * 所属星球：埃里克尔、塞普罗
 */
public class TritiumSeparator {
    public static Block block;

    public static void load() {
        block = new TritiumSeparatorBlock("tritium-separator");
    }
}
