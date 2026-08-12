package example;

import arc.*;
import arc.util.*;
import example.gas.Protium; //重点导入气体
import mindustry.mod.*;

public class Yuansu extends Mod{

    public Yuansu(){
        Log.info("Loaded Yuansu constructor.");
    }

    @Override
    public void loadContent(){
        Log.info("Loading some example content.");
        protium = new Protium();
        protium.load();
    }

}
