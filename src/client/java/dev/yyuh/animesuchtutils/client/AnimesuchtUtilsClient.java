package dev.yyuh.animesuchtutils.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class AnimesuchtUtilsClient implements ClientModInitializer {

    public static boolean highlightGrau      = false;
    public static boolean highlightStandard  = false;
    public static boolean highlightSelten    = false;
    public static boolean highlightEpisch    = false;
    public static boolean highlightLegendaer = false;
    public static boolean highlightSpeziell  = false;
    public static boolean highlightMythisch  = false;
    public static boolean highlightSpezLeg   = false;
    public static boolean highlightGoettlich = false;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("animesucht_utils.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static class ModConfiguration {
        boolean highlightGrau      = false;
        boolean highlightStandard  = false;
        boolean highlightSelten    = false;
        boolean highlightEpisch    = false;
        boolean highlightLegendaer = false;
        boolean highlightSpeziell  = false;
        boolean highlightMythisch  = false;
        boolean highlightSpezLeg   = false;
        boolean highlightGoettlich = false;
    }

    @Override
    public void onInitializeClient() { loadConfiguration(); }

    public static void saveConfiguration() {
        try {
            ModConfiguration cfg = new ModConfiguration();
            cfg.highlightGrau      = highlightGrau;
            cfg.highlightStandard  = highlightStandard;
            cfg.highlightSelten    = highlightSelten;
            cfg.highlightEpisch    = highlightEpisch;
            cfg.highlightLegendaer = highlightLegendaer;
            cfg.highlightSpeziell  = highlightSpeziell;
            cfg.highlightMythisch  = highlightMythisch;
            cfg.highlightSpezLeg   = highlightSpezLeg;
            cfg.highlightGoettlich = highlightGoettlich;
            try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
                GSON.toJson(cfg, writer);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadConfiguration() {
        try {
            if (CONFIG_PATH.toFile().exists()) {
                ModConfiguration cfg = GSON.fromJson(new FileReader(CONFIG_PATH.toFile()), ModConfiguration.class);
                highlightGrau      = cfg.highlightGrau;
                highlightStandard  = cfg.highlightStandard;
                highlightSelten    = cfg.highlightSelten;
                highlightEpisch    = cfg.highlightEpisch;
                highlightLegendaer = cfg.highlightLegendaer;
                highlightSpeziell  = cfg.highlightSpeziell;
                highlightMythisch  = cfg.highlightMythisch;
                highlightSpezLeg   = cfg.highlightSpezLeg;
                highlightGoettlich = cfg.highlightGoettlich;
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
