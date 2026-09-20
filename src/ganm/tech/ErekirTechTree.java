package ganm.tech;

import arc.util.*;
import mindustry.ctype.*;
import mindustry.type.ItemStack;
import mindustry.content.Blocks;
import mindustry.content.Items;
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

        // 氕气分离机 -> 氕气（研究消耗：基础材料）
        TechNode protiumNode = node(ProtiumSeparator.block, ItemStack.with(
            Items.copper, 100,
            Items.lead, 80,
            Items.titanium, 30,
            Items.silicon, 30
        ), () -> {
            nodeProduce(Protium.liquid, () -> {});
        });
        protiumNode.parent = parentNode;
        parentNode.children.add(protiumNode);

        // 氘气分离机 -> 氘气（研究消耗：中级材料，挂在氕气分离机下）
        TechNode deuteriumNode = node(DeuteriumSeparator.block, ItemStack.with(
            Items.copper, 150,
            Items.lead, 120,
            Items.titanium, 60,
            Items.silicon, 60,
            Items.plastanium, 30
        ), () -> {
            nodeProduce(Deuterium.liquid, () -> {});
        });
        deuteriumNode.parent = protiumNode;
        protiumNode.children.add(deuteriumNode);

        // 氚气分离机 -> 氚气（研究消耗：高级材料，挂在氘气分离机下）
        TechNode tritiumNode = node(TritiumSeparator.block, ItemStack.with(
            Items.copper, 200,
            Items.lead, 150,
            Items.titanium, 100,
            Items.silicon, 100,
            Items.plastanium, 60,
            Items.surgeAlloy, 30
        ), () -> {
            nodeProduce(Tritium.liquid, () -> {});
        });
        tritiumNode.parent = deuteriumNode;
        deuteriumNode.children.add(tritiumNode);

        Log.info("Yuansu Erekir tech tree loaded.");
    }
}
