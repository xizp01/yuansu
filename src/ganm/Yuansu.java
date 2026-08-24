package ganm;

import arc.util.*;
import mindustry.ctype.*;
import mindustry.mod.*;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.content.TechTree.TechNode;
import static mindustry.content.TechTree.*;
import ganm.content.YuansuLiquids;
import ganm.content.YuansuBlocks;

public class Yuansu extends Mod{
    public Yuansu(){
        Log.info("Loaded Yuansu constructor.");
    }

    @Override
    public void loadContent(){
        YuansuLiquids.load();
        YuansuBlocks.load();
        Log.info("Yuansu mod content loaded.");
    }

    private TechNode findNode(TechNode node, UnlockableContent content){
        if(node.content == content) return node;
        for(TechNode child : node.children){
            TechNode found = findNode(child, content);
            if(found != null) return found;
        }
        return null;
    }

    @Override
    public void init(){
        TechNode electrolyzerNode = findNode(Planets.erekir.techTree, Blocks.electrolyzer);
        TechNode parentNode = (electrolyzerNode != null) ? electrolyzerNode : Planets.erekir.techTree;

        // 氕气分离机 -> 氕气
        TechNode protiumNode = node(YuansuBlocks.protiumSeparator, () -> {
            nodeProduce(YuansuLiquids.protium, () -> {});
        });
        protiumNode.parent = parentNode;
        parentNode.children.add(protiumNode);

        // 氘气分离机 -> 氘气（挂在氕气分离机下）
        TechNode deuteriumNode = node(YuansuBlocks.deuteriumSeparator, () -> {
            nodeProduce(YuansuLiquids.deuterium, () -> {});
        });
        deuteriumNode.parent = protiumNode;
        protiumNode.children.add(deuteriumNode);

        // 氚气分离机 -> 氚气（挂在氘气分离机下）
        TechNode tritiumNode = node(YuansuBlocks.tritiumSeparator, () -> {
            nodeProduce(YuansuLiquids.tritium, () -> {});
        });
        tritiumNode.parent = deuteriumNode;
        deuteriumNode.children.add(tritiumNode);

        Log.info("Yuansu tech tree attached to Erekir.");
    }
}
