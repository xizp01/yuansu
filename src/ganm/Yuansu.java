package ganm;

import arc.util.*;
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

    @Override
    public void init(){
        // 挂载科技树：分离机挂在电解机下面，氕气挂在分离机下面
        TechNode[] tmp = new TechNode[]{null};
        Planets.serpulo.techTree.each(node -> {
            if(node.content == Blocks.electrolyzer) tmp[0] = node;
        });

        if(tmp[0] != null){
            TechNode separatorNode = node(YuansuBlocks.protiumSeparator, () -> {
                nodeProduce(YuansuLiquids.protium);
            });
            separatorNode.parent = tmp[0];
            tmp[0].children.add(separatorNode);
            Log.info("Yuansu tech tree attached.");
        }else{
            Log.err("Could not find electrolyzer tech node!");
        }
    }
}
