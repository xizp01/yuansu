package ganm.content.entities;
import arc.math.*;
import mindustry.gen.*;
import mindustry.entities.Units;
import ganm.content.status.Radiation;
/**
 * 氚气气体云（完全按照DeepSeek方案实现）
 * 泄漏时生成，持续给范围内单位施加辐射状态，缓慢扩散变淡后消失。
 */
public class TritiumGasCloud {
    public float x, y;
    public float radius;          // 当前辐射半径
    public float maxRadius;       // 最大半径
    public float life;            // 剩余生命
    public float maxLife;         // 总生命
    public float concentration;   // 气体浓度 0-1
    public float statusDuration = 120f; // 辐射状态持续时间（帧）
    public TritiumGasCloud(float x, float y) {
        this.x = x;
        this.y = y;
        this.maxRadius = 40f + Mathf.random(20f);
        this.radius = 10f;
        this.maxLife = 300f + Mathf.random(200f); // 5-8.3秒
        this.life = maxLife;
        this.concentration = 1f;
    }
    public void update() {
        life -= 1f;
        // 缓慢扩散
        radius = Mathf.lerp(radius, maxRadius, 0.02f);
        // 浓度逐渐降低
        concentration = Mathf.clamp(life / maxLife);
        // 浓度高于阈值时，给范围内所有单位施加辐射（DeepSeek核心逻辑）
        if (concentration > 0.1f) {
            Units.nearby(null, x, y, radius, unit -> {
                unit.apply(Radiation.effect, statusDuration);
            });
        }
    }
    public boolean isDead() {
        return life <= 0;
    }
}
