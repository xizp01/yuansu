package ganm.content.entities;
import arc.math.*;
import mindustry.gen.*;
import mindustry.entities.Units;
import ganm.content.status.Radiation;
/**
 * 氚气放射性粒子
 * 从氚气分离机飘散出来，缓慢移动，碰到单位施加辐射状态，一段时间后消失。
 */
public class TritiumParticle {
    public float x, y;
    public float vx, vy;
    public float life;
    public float maxLife;
    public float radius = 6f;
    public TritiumParticle(float x, float y) {
        this.x = x;
        this.y = y;
        // 随机方向缓慢飘散
        float angle = Mathf.random(360f);
        float speed = Mathf.random(0.3f, 0.8f);
        this.vx = Mathf.cosDeg(angle) * speed;
        this.vy = Mathf.sinDeg(angle) * speed + 0.15f; // 略微向上飘
        this.maxLife = Mathf.random(300f, 500f); // 5-8.3秒，飘散更远
        this.life = maxLife;
    }
    public void update() {
        x += vx;
        y += vy;
        // 速度逐渐衰减
        vx *= 0.995f;
        vy *= 0.995f;
        life -= 1f;
        // 用Units.nearby做空间优化，检测范围内的单位
        Units.nearby(null, x, y, radius + 20f, unit -> {
            if (unit.within(x, y, radius + unit.hitSize / 2f)) {
                unit.apply(Radiation.effect, 120f);
            }
        });
    }
    public boolean isDead() {
        return life <= 0;
    }
}
