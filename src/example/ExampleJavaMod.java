package example;

import arc.*;
import arc.util.*;
import mindustry.mod.*;

public class ExampleJavaMod extends Mod{

    public ExampleJavaMod(){
        Log.info("Loaded ExampleJavaMod constructor.");
    }

    @Override
    public void loadContent(){
        Log.info("Loading some example content.");
    }

}
