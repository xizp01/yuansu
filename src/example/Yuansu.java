package example;

import arc.util.Log;
import example.liquid.Protium;
import mindustry.mod.Mod;

public class Yuansu extends Mod{
    public static Protium protium;

    @Override
    public void loadContent(){
        Log.info("【元素模组】开始加载内容");
        protium = new Protium();
        protium.load();
    }
}
