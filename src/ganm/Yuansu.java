package ganm;

import arc.struct.*;
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

    // 递归查找科技树节点
    private TechNode findNode(Seq<TechNode> nodes, UnlockableContent content){
        for(TechNode node : nodes){
            if(node.content == content) return node;
            TechNode found = findNode(node.children, content);
            if(found != null) return found;
        }
        return null;
    }

    @Override
    public void init(){
        // 递归查找埃里克尔科技树中的电解机节点
        TechNode electrolyzerNode = findNode(Planets.erekir.techTree, Blocks.electrolyzer);

        TechNode parentNode;
        if(electrolyzerNode != null){
            parentNode = electrolyzerNode;
            Log.info("Found electrolyzer in Erekir tech tree.");
        }else{
            // 兜底：挂在埃里克尔科技树根节点下
            parentNode = Planets.erekir.techTree.first();
            Log.err("Could not find electrolyzer, attaching to root node instead.");
        }

        TechNode separatorNode = node(YuansuBlocks.protiumSeparator, () -> {
            nodeProduce(YuansuLiquids.protium, () -> {});
        });
        separatorNode.parent = parentNode;
        parentNode.children.add(separatorNode);
        Log.info("Yuansu tech tree attached to Erekir.");
    }
}
