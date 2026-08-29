package ganm.content.status;

import arc.graphics.Color;
import mindustry.type.StatusEffect;

/**
 * 辐射状态
 * 由放射性物质（如氚气）施加，持续造成伤害并降低单位效能。
 */
public class Radiation {
    public static StatusEffect effect;

    public static void load() {
        effect = new StatusEffect("radiation") {{
            color = Color.valueOf("39ff14");
            damage = 0.15f;
            effectChance = 0.08f;
            healthMultiplier = 0.9f;
            speedMultiplier = 0.95f;
            transitionDamage = 12f;
        }};
    }
}
