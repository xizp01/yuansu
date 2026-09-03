package ganm.content.entities;
import arc.math.*;
import mindustry.gen.*;
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
    public float radius = 5f;
    public TritiumParticle(float x, float y) {
        this.x = x;
        this.y = y;
        // 随机方向缓慢飘散
        float angle = Mathf.random(360f);
        float speed = Mathf.random(0.2f, 0.6f);
        this.vx = Mathf.cosDeg(angle) * speed;
        this.vy = Mathf.sinDeg(angle) * speed + 0.1f; // 略微向上飘
        this.maxLife = Mathf.random(200f, 360f); // 3.3-6秒
        this.life = maxLife;
    }
    public void update() {
        x += vx;
        y += vy;
        // 速度逐渐衰减
        vx *= 0.99f;
        vy *= 0.99f;
        life -= 1f;
        // 检测与单位的碰撞，碰到就施加辐射
        Groups.unit.each(unit -> {
            if (unit.within(x, y, radius + unit.hitSize / 2f)) {
                unit.apply(Radiation.effect, 120f);
            }
        });
    }
    public boolean isDead() {
        return life <= 0;
    }
}
