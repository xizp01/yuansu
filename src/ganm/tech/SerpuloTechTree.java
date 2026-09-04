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
import ganm.content.items.SilicaGel;
import ganm.content.items.LithiumChloride;
import ganm.content.items.MOFMaterial;
import ganm.content.blocks.ProtiumSeparator;
import ganm.content.blocks.DeuteriumSeparator;
import ganm.content.blocks.TritiumSeparator;
import ganm.content.blocks.SerpuloElectrolyzer;
import ganm.content.blocks.CondensationWaterCollector;
import ganm.content.blocks.DesiccantWheelCollector;
import ganm.content.blocks.LithiumChlorideCollector;
import ganm.content.blocks.MOFWaterCollector;
import ganm.content.blocks.FogWaterCollector;
import ganm.content.blocks.SilicaGelSynthesizer;
/**
 * 塞普罗星球科技树注册
 * 结构1：水泵 -> 氢气 -> 制氢机 -> 氕气分离机 -> 氘气分离机 -> 氚气分离机
 * 结构2：雾水收集网 -> 冷凝取水器 -> 硅胶合成机 -> 除湿转轮 -> 氯化锂吸附 -> MOF取水器
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
        // 氢气节点（挂在水泵下）
        TechNode hydrogenNode = node(Liquids.hydrogen, () -> {});
        hydrogenNode.parent = parentNode;
        parentNode.children.add(hydrogenNode);
        // 塞普罗制氢机 -> 氧气（副产品）
        TechNode electrolyzerNode = node(SerpuloElectrolyzer.block, () -> {
            nodeProduce(Oxygen.liquid, () -> {});
        });
        electrolyzerNode.parent = hydrogenNode;
        hydrogenNode.children.add(electrolyzerNode);
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
        // ===== 空气取水科技链（挂根节点）=====
        TechNode root = Planets.serpulo.techTree;
        // 雾水收集网（基础被动）
        TechNode fogNode = node(FogWaterCollector.block, () -> {});
        fogNode.parent = root;
        root.children.add(fogNode);
        // 一代：冷凝式空气制水机
        TechNode condensationNode = node(CondensationWaterCollector.block, () -> {});
        condensationNode.parent = fogNode;
        fogNode.children.add(condensationNode);
        // 硅胶合成机 -> 硅胶
        TechNode silicaSynthNode = node(SilicaGelSynthesizer.block, () -> {
            nodeProduce(SilicaGel.item, () -> {});
        });
        silicaSynthNode.parent = condensationNode;
        condensationNode.children.add(silicaSynthNode);
        // 二代：除湿转轮取水器
        TechNode wheelNode = node(DesiccantWheelCollector.block, () -> {});
        wheelNode.parent = silicaSynthNode;
        silicaSynthNode.children.add(wheelNode);
        // 三代：氯化锂复合吸附集水器 -> 氯化锂
        TechNode lithiumNode = node(LithiumChlorideCollector.block, () -> {
            nodeProduce(LithiumChloride.item, () -> {});
        });
        lithiumNode.parent = wheelNode;
        wheelNode.children.add(lithiumNode);
        // 四代：MOF光热空气取水器 -> MOF材料
        TechNode mofNode = node(MOFWaterCollector.block, () -> {
            nodeProduce(MOFMaterial.item, () -> {});
        });
        mofNode.parent = lithiumNode;
        lithiumNode.children.add(mofNode);
        Log.info("Yuansu Serpulo tech tree loaded.");
    }
}
