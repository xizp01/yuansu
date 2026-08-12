package example;

import arc.util.*;
import example.gas.Protium;
import mindustry.mod.*;

public class Yuansu extends Mod{
    public static Protium protium;

    public Yuansu(){
        Log.info("Loaded Yuansu constructor.");
    }

    @Override
    public void loadContent(){
        Log.info("Loading some example content.");

        //注册氕气
        protium = new Protium();
        protium.load();
    }
}
