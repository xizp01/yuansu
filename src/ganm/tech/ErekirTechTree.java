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
import ganm.content.blocks.CondensationWaterCollector;
import ganm.content.blocks.AdsorptionWaterCollector;
import ganm.content.blocks.FogWaterCollector;
/**
 * 埃里克尔星球科技树注册
 * 结构：电解机 -> 氕气分离机 -> 氘气分离机 -> 氚气分离机
 *       雾水收集网 -> 冷凝式空气制水机 -> 吸附式空气集水器
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
        // ===== 空气取水设备分支（挂根节点）=====
        TechNode root = Planets.erekir.techTree;
        // 雾水收集网（基础被动）
        TechNode fogNode = node(FogWaterCollector.block, () -> {});
        fogNode.parent = root;
        root.children.add(fogNode);
        // 冷凝式空气制水机（中级）
        TechNode condensationNode = node(CondensationWaterCollector.block, () -> {});
        condensationNode.parent = fogNode;
        fogNode.children.add(condensationNode);
        // 吸附式空气集水器（高级）
        TechNode adsorptionNode = node(AdsorptionWaterCollector.block, () -> {});
        adsorptionNode.parent = condensationNode;
        condensationNode.children.add(adsorptionNode);
        Log.info("Yuansu Erekir tech tree loaded.");
    }
}
