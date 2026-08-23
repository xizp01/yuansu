package ganm;

import arc.util.*;
import mindustry.mod.*;
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
}
