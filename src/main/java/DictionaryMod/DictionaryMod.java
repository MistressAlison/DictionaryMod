package DictionaryMod;

import DictionaryMod.icons.ImpIcon;
import DictionaryMod.icons.PleadingIcon;
import DictionaryMod.util.TextureLoader;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class DictionaryMod implements
        EditStringsSubscriber,
        PostInitializeSubscriber,
        EditKeywordsSubscriber{
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(DictionaryMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties DictionaryModDefaultSettings = new Properties();

    public static final String ENABLE_MAYBE_SETTING = "enableMaybe";
    public static boolean enableMaybe = false; // The boolean we'll be setting on/off (true/false)


    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Dictionary Mod";
    private static final String AUTHOR = "Mistress Alison";
    private static final String DESCRIPTION = "Makes every word a keyword.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "DictionaryModResources/images/Badge.png";
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public DictionaryMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
      
        setModID("DictionaryMod");
        
        logger.info("Done subscribing");
        
        logger.info("Adding mod settings");

        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        DictionaryModDefaultSettings.setProperty(ENABLE_MAYBE_SETTING, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("DictionaryMod", "DictionaryModConfig", DictionaryModDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableMaybe = config.getBool(ENABLE_MAYBE_SETTING);
            //enableStrongerWantedEffect = config.getBool(FIVE_STAR_WANTED_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Done adding mod settings");
        
    }

    public static void setModID(String ID) {
        modID = ID;
    }
    
    public static String getModID() {
        return modID;
    }
    
    public static void initialize() {
        logger.info("========================= Initializing Yes/No. =========================");
        DictionaryMod dictionaryMod = new DictionaryMod();
        logger.info("========================= /Yes/No Initialized/ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
                logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        CustomIconHelper.addCustomIcon(PleadingIcon.get());
        CustomIconHelper.addCustomIcon(ImpIcon.get());

        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================

    // ================ LOAD THE LOCALIZATION ===================

    private String loadLocalizationIfAvailable(String fileName) {
        if (!Gdx.files.internal(getModID() + "Resources/localization/" + Settings.language.toString().toLowerCase()+ "/" + fileName).exists()) {
            logger.info("Language: " + Settings.language.toString().toLowerCase() + ", not currently supported for " +fileName+".");
            return "eng" + "/" + fileName;
        } else {
            logger.info("Loaded Language: "+ Settings.language.toString().toLowerCase() + ", for "+fileName+".");
            return Settings.language.toString().toLowerCase() + "/" + fileName;
        }
    }

    // ================ /LOAD THE LOCALIZATION/ ===================

    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/"+loadLocalizationIfAvailable("UIstrings.json"));

        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/eng/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(null, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}
