package ganm.content.entities;
import arc.math.*;
/**
 * 氚气放射性粒子（仅作视觉效果）
 * 辐射状态由 TritiumGasCloud 施加。
 */
public class TritiumParticle {
    public float x, y;
    public float vx, vy;
    public float life;
    public float maxLife;
    public float radius = 4f;
    public TritiumParticle(float x, float y) {
        this.x = x;
        this.y = y;
        float angle = Mathf.random(360f);
        float speed = Mathf.random(0.2f, 0.5f);
        this.vx = Mathf.cosDeg(angle) * speed;
        this.vy = Mathf.sinDeg(angle) * speed + 0.1f;
        this.maxLife = Mathf.random(150f, 250f);
        this.life = maxLife;
    }
    public void update() {
        x += vx;
        y += vy;
        vx *= 0.99f;
        vy *= 0.99f;
        life -= 1f;
    }
    public boolean isDead() {
        return life <= 0;
    }
}
