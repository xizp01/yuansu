package ganm.tech;

import arc.util.*;
import mindustry.ctype.*;
import mindustry.content.Blocks;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.content.TechTree.TechNode;
import static mindustry.content.TechTree.*;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.liquids.Oxygen;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.content.blocks.SerpuloElectrolyzer;

/**
 * 塞普罗星球科技树注册
 * 结构：制氢机 -> 氕气分离机 -> 氘气分离机 -> 氚气分离机
 * 同时将原版氢气开放到塞普罗星球
 */
public class SerpuloTechTree {

    private static TechNode findNode(TechNode node, UnlockableContent content) {
        if (node.content == content) return node;
        for (TechNode child : node.children) {
            TechNode found = findNode(child, content);
            if (found != null) return found;
        }
        return null;
    }

    public static void load() {
        // 将原版氢气开放到塞普罗星球
        Liquids.hydrogen.shownPlanets.add(Planets.serpulo);

        // 查找塞普罗水泵节点作为父节点，找不到则挂根节点
        TechNode pumpNode = findNode(Planets.serpulo.techTree, Blocks.waterExtractor);
        TechNode parentNode = (pumpNode != null) ? pumpNode : Planets.serpulo.techTree;

        // 塞普罗制氢机 -> 氧气（副产品）
        TechNode electrolyzerNode = node(SerpuloElectrolyzer.block, () -> {
            nodeProduce(Oxygen.liquid, () -> {});
        });
        electrolyzerNode.parent = parentNode;
        parentNode.children.add(electrolyzerNode);

        // 氕气分离机 -> 氕气
        TechNode protiumNode = node(ProtiumSeparator.block, () -> {
            nodeProduce(Protium.liquid, () -> {});
        });
        protiumNode.parent = electrolyzerNode;
        electrolyzerNode.children.add(protiumNode);

        // 氘气分离机 -> 氘气
        TechNode deuteriumNode = node(DeuteriumSeparator.block, () -> {
            nodeProduce(Deuterium.liquid, () -> {});
        });
        deuteriumNode.parent = protiumNode;
        protiumNode.children.add(deuteriumNode);

        // 氚气分离机 -> 氚气
        TechNode tritiumNode = node(TritiumSeparator.block, () -> {
            nodeProduce(Tritium.liquid, () -> {});
        });
        tritiumNode.parent = deuteriumNode;
        deuteriumNode.children.add(tritiumNode);

        Log.info("Yuansu Serpulo tech tree loaded.");
    }
}
