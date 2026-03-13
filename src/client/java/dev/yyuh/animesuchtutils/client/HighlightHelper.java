package dev.yyuh.animesuchtutils.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class HighlightHelper {

    private static final int[][] CIRCLE = {
        {0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0},
        {0,0,0,1,1,1,2,2,2,2,1,1,1,0,0,0},
        {0,0,1,1,2,2,2,2,2,2,2,2,1,1,0,0},
        {0,1,1,2,2,2,2,2,2,2,2,2,2,1,1,0},
        {0,1,2,2,2,2,2,2,2,2,2,2,2,2,1,0},
        {1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1},
        {0,1,2,2,2,2,2,2,2,2,2,2,2,2,1,0},
        {0,1,1,2,2,2,2,2,2,2,2,2,2,1,1,0},
        {0,0,1,1,2,2,2,2,2,2,2,2,1,1,0,0},
        {0,0,0,1,1,1,2,2,2,2,1,1,1,0,0,0},
        {0,0,0,0,0,1,1,1,1,1,1,0,0,0,0,0},
    };

    private static final boolean[][] STAR = {
        {false, false, true,  false, false},
        {false, true,  true,  true,  false},
        {true,  true,  true,  true,  true },
        {false, true,  true,  true,  false},
        {true,  false, true,  false, true },
    };

    // Vanilla §-Farben als RGB-Ints
    private static final int RGB_GRAY         = 0xAAAAAA; // §7
    private static final int RGB_GREEN        = 0x55FF55; // §a
    private static final int RGB_AQUA         = 0x55FFFF; // §b
    private static final int RGB_BLUE         = 0x5555FF; // §9
    private static final int RGB_GOLD         = 0xFFAA00; // §6
    private static final int RGB_LIGHT_PURPLE = 0xFF55FF; // §d
    private static final int RGB_DARK_RED     = 0xAA0000; // §4

    // Besonders: &#0575E6
    private static final int RGB_ZAUBER = 0x0575E6;

    // Göttlich: alle 4 Gradient-Stops
    private static final int[] RGB_GOETTLICH = { 0xCD06FF, 0xE0049F, 0xEC0260, 0xF90120 };

    // Direkter RGB-Vergleich – zuverlässiger als TextColor.equals() bei Hex-Farben
    private static boolean containsRgb(Text text, int rgb) {
        TextColor c = text.getStyle().getColor();
        if (c != null && c.getRgb() == rgb) return true;
        for (Text sibling : text.getSiblings()) {
            if (containsRgb(sibling, rgb)) return true;
        }
        return false;
    }

    private static boolean isGoettlich(Text text) {
        for (int rgb : RGB_GOETTLICH) {
            if (containsRgb(text, rgb)) return true;
        }
        return false;
    }

    public static boolean hasMaxedEnchantment(ItemStack stack) {
        if (stack.isEmpty()) return false;
        var enchantments = EnchantmentHelper.getEnchantments(stack);
        for (var entry : enchantments.getEnchantments()) {
            int level = enchantments.getLevel(entry);
            int maxLevel = entry.value().getMaxLevel();
            if (level >= maxLevel && maxLevel > 0) return true;
        }
        return false;
    }

    public static void drawStar(DrawContext context, Slot slot) {
        for (int sy = 0; sy < 5; sy++)
            for (int sx = 0; sx < 5; sx++)
                if (STAR[sy][sx])
                    context.fill(slot.x + sx, slot.y + sy,
                                 slot.x + sx + 1, slot.y + sy + 1, 0xFFFFD700);
    }

    public static void applyEnchantHighlight(DrawContext context, Slot slot) {
        boolean anyHighlight = AnimesuchtUtilsClient.highlightGrau
                || AnimesuchtUtilsClient.highlightStandard
                || AnimesuchtUtilsClient.highlightSelten
                || AnimesuchtUtilsClient.highlightEpisch
                || AnimesuchtUtilsClient.highlightLegendaer
                || AnimesuchtUtilsClient.highlightSpeziell
                || AnimesuchtUtilsClient.highlightMythisch
                || AnimesuchtUtilsClient.highlightSpezLeg
                || AnimesuchtUtilsClient.highlightGoettlich;
        if (!anyHighlight) return;

        ItemStack itemStack = slot.getStack();
        if (itemStack.isEmpty()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        Item.TooltipContext ctx = Item.TooltipContext.create(client.world);
        List<Text> tooltip = itemStack.getTooltip(ctx, client.player, TooltipType.BASIC);

        boolean isGrau      = false;
        boolean isStandard  = false;
        boolean isSelten    = false;
        boolean isEpisch    = false;
        boolean isLegendaer = false;
        boolean isSpeziell  = false;
        boolean isMythisch  = false;
        boolean isSpezLeg   = false;
        boolean isGoettlich = false;

        for (int i = 1; i < tooltip.size(); i++) {
            Text line = tooltip.get(i);
            if (isGoettlich(line))                   isGoettlich = true;
            if (containsRgb(line, RGB_ZAUBER))       isSpezLeg   = true;
            if (containsRgb(line, RGB_DARK_RED))     isMythisch  = true;
            if (containsRgb(line, RGB_LIGHT_PURPLE)) isSpeziell  = true;
            if (containsRgb(line, RGB_GOLD))         isLegendaer = true;
            if (containsRgb(line, RGB_BLUE))         isEpisch    = true;
            if (containsRgb(line, RGB_AQUA))         isSelten    = true;
            if (containsRgb(line, RGB_GREEN))        isStandard  = true;
            if (containsRgb(line, RGB_GRAY))         isGrau      = true;
        }

        // Keine Priorität – alle erkannten Tiers werden gleichzeitig in der Torte angezeigt
        // (mehrere Verzauberungsstufen auf Tools/Rüstung ergeben mehrfarbige Tortenstücke)

        List<int[]> colors = new ArrayList<>();
        if (AnimesuchtUtilsClient.highlightGoettlich && isGoettlich) colors.add(new int[]{0xCD, 0x06, 0xFF, 0x55});
        if (AnimesuchtUtilsClient.highlightSpezLeg   && isSpezLeg)   colors.add(new int[]{0x05, 0x75, 0xE6, 0x55});
        if (AnimesuchtUtilsClient.highlightMythisch  && isMythisch)  colors.add(new int[]{0xAA, 0x00, 0x00, 0x55});
        if (AnimesuchtUtilsClient.highlightSpeziell  && isSpeziell)  colors.add(new int[]{0xFF, 0x55, 0xFF, 0x55});
        if (AnimesuchtUtilsClient.highlightLegendaer && isLegendaer) colors.add(new int[]{0xFF, 0xAA, 0x00, 0x55});
        if (AnimesuchtUtilsClient.highlightEpisch    && isEpisch)    colors.add(new int[]{0x55, 0x55, 0xFF, 0x55});
        if (AnimesuchtUtilsClient.highlightSelten    && isSelten)    colors.add(new int[]{0x55, 0xFF, 0xFF, 0x55});
        if (AnimesuchtUtilsClient.highlightStandard  && isStandard)  colors.add(new int[]{0x55, 0xFF, 0x55, 0x55});
        if (AnimesuchtUtilsClient.highlightGrau      && isGrau)      colors.add(new int[]{0xAA, 0xAA, 0xAA, 0x55});

        if (colors.isEmpty()) {
            if (hasMaxedEnchantment(itemStack)) drawStar(context, slot);
            return;
        }

        int total = colors.size();
        for (int py = 0; py < 16; py++) {
            for (int px = 0; px < 16; px++) {
                int type = CIRCLE[py][px];
                if (type == 0) continue;
                double dx = px + 0.5 - 7.5;
                double dy = py + 0.5 - 7.5;
                double angle = Math.toDegrees(Math.atan2(dx, -dy));
                if (angle < 0) angle += 360.0;
                int segIndex = (int) (angle / (360.0 / total)) % total;
                int[] c = colors.get(segIndex);
                int alpha = (type == 1) ? 0x55 : c[3] + 0x55;
                alpha = Math.min(alpha, 0xAA);
                int argb = (alpha << 24) | (c[0] << 16) | (c[1] << 8) | c[2];
                context.fill(slot.x + px, slot.y + py, slot.x + px + 1, slot.y + py + 1, argb);
            }
        }

        if (hasMaxedEnchantment(itemStack)) drawStar(context, slot);
    }
}
