package ganm.tech;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.type.ItemStack;
import mindustry.content.Blocks;
import mindustry.content.Items;
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
import ganm.content.blocks.SteamGenerator;
/**
 * 塞普罗星球科技树注册
 * 结构：水泵 -> 氢气 -> 制氢机 -> 氕气分离机 -> 氘气分离机 -> 氚气分离机
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
        // 氢气节点（挂在水泵下，无消耗）
        TechNode hydrogenNode = node(Liquids.hydrogen, () -> {});
        hydrogenNode.parent = parentNode;
        parentNode.children.add(hydrogenNode);

        // 蒸汽发生器 -> 水蒸气（挂在水泵旁边，研究消耗：基础材料）
        TechNode steamGenNode = node(SteamGenerator.block, ItemStack.with(
            Items.copper, 80,
            Items.lead, 60,
            Items.titanium, 20,
            Items.silicon, 25,
            Items.metaglass, 15
        ), () -> {
            nodeProduce(ganm.content.liquids.Steam.liquid, () -> {});
        });
        steamGenNode.parent = parentNode;
        parentNode.children.add(steamGenNode);

        // 塞普罗制氢机 -> 氧气（副产品，研究消耗：基础材料）
        TechNode electrolyzerNode = node(SerpuloElectrolyzer.block, ItemStack.with(
            Items.copper, 120,
            Items.lead, 100,
            Items.titanium, 40,
            Items.silicon, 50
        ), () -> {
            nodeProduce(Oxygen.liquid, () -> {});
        });
        electrolyzerNode.parent = hydrogenNode;
        hydrogenNode.children.add(electrolyzerNode);
        // 氕气分离机 -> 氕气（研究消耗：基础材料）
        TechNode protiumNode = node(ProtiumSeparator.block, ItemStack.with(
            Items.copper, 100,
            Items.lead, 80,
            Items.titanium, 30,
            Items.silicon, 30
        ), () -> {
            nodeProduce(Protium.liquid, () -> {});
        });
        protiumNode.parent = electrolyzerNode;
        electrolyzerNode.children.add(protiumNode);
        // 氘气分离机 -> 氘气（研究消耗：中级材料）
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
        // 氚气分离机 -> 氚气（研究消耗：高级材料）
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
        Log.info("Yuansu Serpulo tech tree loaded.");
    }
}
