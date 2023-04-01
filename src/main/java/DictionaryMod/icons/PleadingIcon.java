package DictionaryMod.icons;

import DictionaryMod.DictionaryMod;
import DictionaryMod.util.TextureLoader;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class PleadingIcon extends AbstractCustomIcon {
    public static String ID = DictionaryMod.makeID("Pleading");
    public static PleadingIcon singleton = null;
    public PleadingIcon() {
        super(ID, TextureLoader.getTexture(DictionaryMod.getModID()+"Resources/images/pleading-face.png"));
    }

    public static PleadingIcon get() {
        if (singleton == null) {
            singleton = new PleadingIcon();
        }
        return singleton;
    }
}
