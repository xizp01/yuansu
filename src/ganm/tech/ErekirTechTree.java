package ganm.tech;

import arc.util.*;
import mindustry.ctype.*;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.content.TechTree.TechNode;
import static mindustry.content.TechTree.*;
import ganm.content.liquids.Protium;
import ganm.content.liquids.Deuterium;
import ganm.content.liquids.Tritium;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;

/**
 * 埃里克尔星球科技树注册
 * 结构：电解机 -> 氕气分离机 -> 氘气分离机 -> 氚气分离机
 */
public class ErekirTechTree {

    private static TechNode findNode(TechNode node, UnlockableContent content) {
        if (node.content == content) return node;
        for (TechNode child : node.children) {
            TechNode found = findNode(child, content);
            if (found != null) return found;
        }
        return null;
    }

    public static void load() {
        TechNode electrolyzerNode = findNode(Planets.erekir.techTree, Blocks.electrolyzer);
        TechNode parentNode = (electrolyzerNode != null) ? electrolyzerNode : Planets.erekir.techTree;

        // 氕气分离机 -> 氕气
        TechNode protiumNode = node(ProtiumSeparator.block, () -> {
            nodeProduce(Protium.liquid, () -> {});
        });
        protiumNode.parent = parentNode;
        parentNode.children.add(protiumNode);

        // 氘气分离机 -> 氘气（挂在氕气分离机下）
        TechNode deuteriumNode = node(DeuteriumSeparator.block, () -> {
            nodeProduce(Deuterium.liquid, () -> {});
        });
        deuteriumNode.parent = protiumNode;
        protiumNode.children.add(deuteriumNode);

        // 氚气分离机 -> 氚气（挂在氘气分离机下）
        TechNode tritiumNode = node(TritiumSeparator.block, () -> {
            nodeProduce(Tritium.liquid, () -> {});
        });
        tritiumNode.parent = deuteriumNode;
        deuteriumNode.children.add(tritiumNode);

        Log.info("Yuansu Erekir tech tree loaded.");
    }
}
