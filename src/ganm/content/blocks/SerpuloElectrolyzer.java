package ganm.content.blocks;

import mindustry.world.Block;

/**
 * 塞普罗电解制氢机
 * 通过电解水同时制取氢气和氧气，为塞普罗同位素分离产业链提供原料。
 * 所属星球：塞普罗
 */
public class SerpuloElectrolyzer {
    public static Block block;

    public static void load() {
        block = new SerpuloElectrolyzerBlock("serpulo-electrolyzer");
    }
}
