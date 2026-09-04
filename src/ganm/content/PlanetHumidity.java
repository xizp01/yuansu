package ganm.content;
import mindustry.content.Planets;
import mindustry.type.Planet;
import mindustry.Vars;
import java.util.HashMap;
/**
 * 星球全局湿度配置
 * 湿度范围 0-100，影响空气取水设备的产量倍率。
 * 湿度越高，空气中水蒸气越多，取水效率越高。
 */
public class PlanetHumidity {
    // 各星球湿度值（0-100）
    private static final HashMap<Planet, Float> humidityMap = new HashMap<>();
    static {
        humidityMap.put(Planets.serpulo, 75f);  // 塞普罗：潮湿星球，湿度高
        humidityMap.put(Planets.erekir, 35f);   // 埃里克尔：干旱星球，湿度低
    }
    /**
     * 获取指定星球的湿度值
     */
    public static float getHumidity(Planet planet) {
        return humidityMap.getOrDefault(planet, 50f);
    }
    /**
     * 获取当前游戏所在星球的湿度值
     */
    public static float getCurrentHumidity() {
        try {
            if (Vars.state.rules.sector != null && Vars.state.rules.sector.planet != null) {
                return getHumidity(Vars.state.rules.sector.planet);
            }
        } catch (Exception e) {
            // 忽略异常，返回默认值
        }
        return 50f;
    }
    /**
     * 根据当前湿度计算产量倍率
     * 湿度 0   -> 倍率 0.3（几乎无水汽）
     * 湿度 50  -> 倍率 0.9（中等）
     * 湿度 100 -> 倍率 1.5（极潮湿）
     */
    public static float getProductionMultiplier() {
        float humidity = getCurrentHumidity();
        return 0.3f + (humidity / 100f) * 1.2f;
    }
}
