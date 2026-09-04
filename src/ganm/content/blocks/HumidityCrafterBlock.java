package ganm.content.blocks;
import mindustry.world.blocks.production.GenericCrafter;
/**
 * 受湿度影响的制作方块基类
 * 通过自定义Build类，在update中根据湿度倍率调整制作进度。
 */
public abstract class HumidityCrafterBlock extends GenericCrafter {
    public HumidityCrafterBlock(String name) {
        super(name);
    }
    /**
     * 获取当前湿度倍率，由子类实现
     */
    public abstract float getHumidityMultiplier();
    public class Build extends GenericCrafterBuild {
        private float lastProgress = 0f;
        @Override
        public void update() {
            lastProgress = progress;
            super.update();
            // 根据湿度倍率调整本帧的进度增加量
            if (efficiency > 0) {
                float multiplier = getHumidityMultiplier();
                if (multiplier != 1.0f) {
                    float actualIncrease = progress - lastProgress;
                    float targetIncrease = actualIncrease * multiplier;
                    progress = lastProgress + targetIncrease;
                }
            }
        }
    }
}
