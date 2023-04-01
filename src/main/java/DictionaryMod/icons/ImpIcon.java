package DictionaryMod.icons;

import DictionaryMod.DictionaryMod;
import DictionaryMod.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class ImpIcon extends AbstractCustomIcon {
    public static String ID = DictionaryMod.makeID("Imp");
    public static ImpIcon singleton = null;
    public ImpIcon() {
        super(ID, TextureLoader.getTexture(DictionaryMod.getModID()+"Resources/images/smiling-imp.png"));
    }

    public static ImpIcon get() {
        if (singleton == null) {
            singleton = new ImpIcon();
        }
        return singleton;
    }
}
