package ganm.content.liquids;
import arc.graphics.Color;
import arc.math.Angles;
import arc.util.Tmp;
import mindustry.type.Liquid;
import mindustry.content.Planets;
import mindustry.entities.Effect;
import mindustry.entities.EffectContainer;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import ganm.content.status.Scalding;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
/**
 * 水蒸气
 * 高温气体，泄漏后在周围形成蒸汽云，单位进入范围会被烫伤。
 * 所属星球：埃里克尔、塞普罗
 */
public class Steam {
    public static Liquid liquid;
    public static void load() {
        // 自定义蒸汽泄漏效果：带update，每10帧扫描周围单位施加烫伤
        Effect steamLeak = new Effect(60f, e -> {
            // render：绘制灰白色蒸汽团
            Draw.color(Color.valueOf("cccccc"), Color.valueOf("aaaaaa"), e.fin());
            Angles.randLenVectors(e.id, 4, 4f + e.fin() * 6f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 2.0f * e.fout());
            });
            Draw.color();
        });
        // 给Effect添加update逻辑：每10帧检测周围单位
        steamLeak.update = e -> {
            if (e.time % 10 < 1) {
                float radius = 12f; // 影响半径
                Units.nearby(null, e.x, e.y, radius, unit -> {
                    if (unit.isValid() && !unit.flagged) {
                        unit.apply(Scalding.effect, 60f);
                    }
                });
            }
        };
        liquid = new Liquid("steam", Color.valueOf("cccccc")) {{
            gas = true; // 气体
            flammability = 0f;
            viscosity = 0.1f;
            temperature = 1.0f; // 高温
            heatCapacity = 0.5f;
            barColor = Color.valueOf("aaaaaa");
            leakEffect = steamLeak; // 泄漏效果
            shownPlanets.add(Planets.erekir);
            shownPlanets.add(Planets.serpulo);
        }};
    }
}
