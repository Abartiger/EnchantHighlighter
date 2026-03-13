package dev.yyuh.animesuchtutils.client.mixin;

import dev.yyuh.animesuchtutils.client.AnimesuchtUtilsClient;
import dev.yyuh.animesuchtutils.client.HighlightHelper;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class ChestSearchMixin extends Screen {

    protected ChestSearchMixin(Text title) { super(title); }

    @Unique private boolean isEmptyString = true;
    @Unique private TextFieldWidget itemSearchBox;
    @Unique private static String lastSearchQuery = "";

    @Unique private ButtonWidget btnGoettlich;
    @Unique private ButtonWidget btnMythisch;
    @Unique private ButtonWidget btnSpeziell;
    @Unique private ButtonWidget btnSpezLeg;
    @Unique private ButtonWidget btnLegendaer;
    @Unique private ButtonWidget btnEpisch;
    @Unique private ButtonWidget btnSelten;
    @Unique private ButtonWidget btnStandard;
    @Unique private ButtonWidget btnGrau;
    @Unique private ButtonWidget btnReset;

    @Inject(at = @At("RETURN"), method = "init()V")
    private void addWidgets(CallbackInfo info) {
        int btnW = 90;
        int btnH = 18;
        int gap  = 4;
        int cx   = this.width / 2;

        // Pyramide von unten nach oben:
        // Basis  [3]: Selten | Standard | Grau
        // Reihe 3[3]: Besonders | Legendar | Episch
        // Reihe 2[2]: Mythisch | Speziell
        // Spitze [1]: Gottlich

        int rowBase = this.height - 22;
        int row3Y   = rowBase - btnH - gap;
        int row2Y   = row3Y  - btnH - gap;
        int row1Y   = row2Y  - btnH - gap;

        int w3 = 3 * btnW + 2 * gap;
        int w2 = 2 * btnW + 1 * gap;

        int startBase = cx - w3 / 2;
        int start3    = cx - w3 / 2;
        int start2    = cx - w2 / 2;
        int start1    = cx - btnW / 2;

        // Basis: Selten | Standard | Grau
        btnSelten = ButtonWidget.builder(
            buildLabel("Selten", "§b", AnimesuchtUtilsClient.highlightSelten), b -> {
                AnimesuchtUtilsClient.highlightSelten = !AnimesuchtUtilsClient.highlightSelten;
                b.setMessage(buildLabel("Selten", "§b", AnimesuchtUtilsClient.highlightSelten));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(startBase, rowBase, btnW, btnH).build();

        btnStandard = ButtonWidget.builder(
            buildLabel("Standard", "§a", AnimesuchtUtilsClient.highlightStandard), b -> {
                AnimesuchtUtilsClient.highlightStandard = !AnimesuchtUtilsClient.highlightStandard;
                b.setMessage(buildLabel("Standard", "§a", AnimesuchtUtilsClient.highlightStandard));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(startBase + btnW + gap, rowBase, btnW, btnH).build();

        btnGrau = ButtonWidget.builder(
            buildLabel("Grau", "§7", AnimesuchtUtilsClient.highlightGrau), b -> {
                AnimesuchtUtilsClient.highlightGrau = !AnimesuchtUtilsClient.highlightGrau;
                b.setMessage(buildLabel("Grau", "§7", AnimesuchtUtilsClient.highlightGrau));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(startBase + 2 * (btnW + gap), rowBase, btnW, btnH).build();

        // Reihe 3: Besonders | Legendar | Episch
        btnSpezLeg = ButtonWidget.builder(
            buildLabel("Besonders", "§3", AnimesuchtUtilsClient.highlightSpezLeg), b -> {
                AnimesuchtUtilsClient.highlightSpezLeg = !AnimesuchtUtilsClient.highlightSpezLeg;
                b.setMessage(buildLabel("Besonders", "§3", AnimesuchtUtilsClient.highlightSpezLeg));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start3, row3Y, btnW, btnH).build();

        btnLegendaer = ButtonWidget.builder(
            buildLabel("Legendaer", "§6", AnimesuchtUtilsClient.highlightLegendaer), b -> {
                AnimesuchtUtilsClient.highlightLegendaer = !AnimesuchtUtilsClient.highlightLegendaer;
                b.setMessage(buildLabel("Legendaer", "§6", AnimesuchtUtilsClient.highlightLegendaer));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start3 + btnW + gap, row3Y, btnW, btnH).build();

        btnEpisch = ButtonWidget.builder(
            buildLabel("Episch", "§9", AnimesuchtUtilsClient.highlightEpisch), b -> {
                AnimesuchtUtilsClient.highlightEpisch = !AnimesuchtUtilsClient.highlightEpisch;
                b.setMessage(buildLabel("Episch", "§9", AnimesuchtUtilsClient.highlightEpisch));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start3 + 2 * (btnW + gap), row3Y, btnW, btnH).build();

        // Reihe 2: Mythisch | Speziell
        btnMythisch = ButtonWidget.builder(
            buildLabel("Mythisch", "§c", AnimesuchtUtilsClient.highlightMythisch), b -> {
                AnimesuchtUtilsClient.highlightMythisch = !AnimesuchtUtilsClient.highlightMythisch;
                b.setMessage(buildLabel("Mythisch", "§c", AnimesuchtUtilsClient.highlightMythisch));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start2, row2Y, btnW, btnH).build();

        btnSpeziell = ButtonWidget.builder(
            buildLabel("Speziell", "§d", AnimesuchtUtilsClient.highlightSpeziell), b -> {
                AnimesuchtUtilsClient.highlightSpeziell = !AnimesuchtUtilsClient.highlightSpeziell;
                b.setMessage(buildLabel("Speziell", "§d", AnimesuchtUtilsClient.highlightSpeziell));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start2 + btnW + gap, row2Y, btnW, btnH).build();

        // Spitze: Gottlich
        btnGoettlich = ButtonWidget.builder(
            buildLabel("Gottlich", "§5", AnimesuchtUtilsClient.highlightGoettlich), b -> {
                AnimesuchtUtilsClient.highlightGoettlich = !AnimesuchtUtilsClient.highlightGoettlich;
                b.setMessage(buildLabel("Gottlich", "§5", AnimesuchtUtilsClient.highlightGoettlich));
                AnimesuchtUtilsClient.saveConfiguration();
            }).dimensions(start1, row1Y, btnW, btnH).build();

        this.addDrawableChild(btnSelten);
        this.addDrawableChild(btnStandard);
        this.addDrawableChild(btnGrau);
        this.addDrawableChild(btnSpezLeg);
        this.addDrawableChild(btnLegendaer);
        this.addDrawableChild(btnEpisch);
        this.addDrawableChild(btnMythisch);
        this.addDrawableChild(btnSpeziell);
        this.addDrawableChild(btnGoettlich);

        // Suchbox uber der Pyramide
        int searchW = 110;
        int searchX = cx - (searchW + 22) / 2;
        int searchY = row1Y - 24;

        itemSearchBox = new TextFieldWidget(
                this.textRenderer, searchX, searchY, searchW, 16,
                Text.literal("Suche..."));
        this.addSelectableChild(itemSearchBox);
        itemSearchBox.setText(lastSearchQuery);
        itemSearchBox.setChangedListener(str -> {
            isEmptyString = str.trim().isEmpty();
            lastSearchQuery = str;
        });
        isEmptyString = lastSearchQuery.trim().isEmpty();
        this.setFocused(null);

        btnReset = ButtonWidget.builder(Text.literal("§c✕"), b -> {
            itemSearchBox.setText("");
            lastSearchQuery = "";
        }).dimensions(searchX + searchW + 3, searchY - 1, 18, 18).build();
        this.addDrawableChild(btnReset);
    }

    @Unique
    private static String stripFormatting(String text) {
        return text.replaceAll("§[0-9a-fk-or]", "");
    }

    @Unique
    private static Text buildLabel(String name, String color, boolean active) {
        return active
            ? Text.literal("§a[✔] " + color + name)
            : Text.literal("§7[ ] " + name);
    }

    @Inject(at = @At("HEAD"),
            method = "drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V")
    private void renderMatchingResults(DrawContext context, Slot slot, CallbackInfo info) {
        if (!isEmptyString && itemSearchBox != null) {
            String searchText = itemSearchBox.getText().trim().toLowerCase();
            ItemStack stack = slot.getStack();
            boolean matches = false;
            if (!stack.isEmpty()) {
                if (stripFormatting(stack.getName().getString()).toLowerCase().contains(searchText)) {
                    matches = true;
                }
                if (!matches) {
                    Item.TooltipContext ctx = Item.TooltipContext.create(this.client.world);
                    List<Text> tooltip = stack.getTooltip(ctx, this.client.player, TooltipType.BASIC);
                    for (Text line : tooltip) {
                        if (stripFormatting(line.getString()).toLowerCase().contains(searchText)) {
                            matches = true;
                            break;
                        }
                    }
                }
            }
            context.fillGradient(slot.x, slot.y, slot.x + 16, slot.y + 16,
                matches ? 0xAA00FF00 : 0xAAFF0000,
                matches ? 0xAA00FF00 : 0xAAFF0000);
            return;
        }

        HighlightHelper.applyEnchantHighlight(context, slot);
    }

    @Inject(at = @At("RETURN"),
            method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
    private void renderExtras(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (itemSearchBox != null) itemSearchBox.render(context, mouseX, mouseY, delta);
    }

    // Direkte Overrides – kein @Inject nötig, da ChestSearchMixin Screen extended.
    // Blockiert alle MC-Keybinds wenn die Suchbox fokussiert ist.
    @Inject(at = @At("HEAD"), method = "keyPressed(Lnet/minecraft/client/input/KeyInput;)Z", cancellable = true)
    private void interceptKeyPress(KeyInput input, CallbackInfoReturnable<Boolean> info) {
        if (itemSearchBox == null || !itemSearchBox.isFocused()) return;
        int keyCode = input.key();
        if (keyCode == 256) { // ESCAPE
            itemSearchBox.setFocused(false);
            this.setFocused(null);
        } else if (keyCode == 259) { // BACKSPACE
            String t = itemSearchBox.getText();
            if (!t.isEmpty()) {
                itemSearchBox.setText(t.substring(0, t.length() - 1));
                lastSearchQuery = itemSearchBox.getText();
                isEmptyString = lastSearchQuery.trim().isEmpty();
            }
        } else if (keyCode == 261) { // DELETE
            itemSearchBox.setText("");
            lastSearchQuery = "";
            isEmptyString = true;
        }
        info.setReturnValue(true);
    }

    // charTyped ist NUR auf Screen definiert, HandledScreen überschreibt es nicht.
    // Deshalb @Override statt @Inject – kein Feather-Konflikt möglich.
    @Override
    public boolean charTyped(CharInput input) {
        if (itemSearchBox != null && itemSearchBox.isFocused()) {
            boolean result = itemSearchBox.charTyped(input);
            lastSearchQuery = itemSearchBox.getText();
            isEmptyString = lastSearchQuery.trim().isEmpty();
            return result;
        }
        return super.charTyped(input);
    }
}
