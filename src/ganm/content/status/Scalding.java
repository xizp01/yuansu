package ganm.content.status;
import arc.graphics.Color;
import mindustry.type.StatusEffect;
/**
 * 烫伤状态效果
 * 站在水蒸气水坑上的单位会持续受到灼烧伤害
 */
public class Scalding {
    public static StatusEffect effect;
    public static void load() {
        effect = new StatusEffect("scalding") {{
            color = Color.valueOf("ff9955");
            damage = 0.35f;
            effect = mindustry.content.Fx.hot;
            transitionDamage = 8f;
        }};
    }
}
