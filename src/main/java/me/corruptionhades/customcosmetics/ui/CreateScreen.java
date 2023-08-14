package me.corruptionhades.customcosmetics.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomAnimation;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.cosmetic.custom.anim.Custom3ValueAnimation;
import me.corruptionhades.customcosmetics.ui.comp.Preset;
import me.corruptionhades.customcosmetics.ui.comp.impl.*;
import me.corruptionhades.customcosmetics.ui.comp.impl.Button;
import me.corruptionhades.customcosmetics.ui.comp.impl.TextField;
import me.corruptionhades.customcosmetics.ui.comp.impl.animation.Animation3ValueComp;
import me.corruptionhades.customcosmetics.ui.comp.impl.animation.AnimationEditComp;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import me.corruptionhades.customcosmetics.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class CreateScreen extends Preset {

    private final MainScreen parent;
    private CustomCosmetic cosmetic;

    private final AnimationEditComp editComp;

    private Custom3ValueAnimation editing;
    private TextField editingNameField;
    private DropDown editingType;
    private ToggleButton toggleButton;

    private Button back;

    private final List<Animation3ValueComp> comps = new ArrayList<>();

    private int mouseX, mouseY;
    private int scrollNow = 0;

    public CreateScreen(MainScreen parent) {
        this.parent = parent;
        editComp = new AnimationEditComp();
        if(cosmetic == null) return;
        for (Custom3ValueAnimation customAnimation : cosmetic.getCustomAnimations()) {
            comps.add(new Animation3ValueComp(this, customAnimation));
        }

        back = new Button("Back", new Color(97, 187, 95));
        back.addClickListener(() -> {
            parent.setViewMode(ViewMode.Import);
            parent.closeSettings();
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        int sf = guiUtils.getGuiScale();

        double posX = parent.posX, posY = parent.posY, width = parent.width;
        double settingsX = parent.getSettingsX(), settingsY = parent.getSettingsY(), settingsWidth = (140 * sf);

        double stringWidth = FontUtil.big.getStringWidth("Animations");
        double middle = ((width) / 2) - (stringWidth / 2);
        FontUtil.big.drawString(context, "Animations", posX + (int) middle, (int) posY + (10 * sf), -1);

        guiUtils.drawRoundedOutline(context, posX + (35 * sf), posY + (55 * sf), posX + ((width / 2) * 1) + (60 * sf), posY + ((85 + 170) * sf),15, 6, new Color(97, 187, 95), new Color(126, 240, 124));

        editComp.render(context, mouseX, mouseY, settingsX + (10 * sf), settingsY + (60 * sf), settingsWidth - (20 * sf), 25 * sf);

        // posX + 35, posY + 55, posX + ((width / 2) * 1) + 60, posY + 85 + 160

        int frameTop = (int) (posY + (65 * sf));
        int frameHeight = (int) (posY + ((85 + 160) * sf)) - frameTop;

        int compHeight = 120 * sf;
        int compOffset = 10 * sf;
        int compAmount = comps.size();
        int borderOffset = 10 * sf;

        int totalCubeHeight = (compHeight + compOffset) * compAmount + borderOffset;
        int maxScrollAmount = -((borderOffset + totalCubeHeight) - frameHeight) + compOffset;

        if(scrollNow >= 0) {
            scrollNow = 0;
        }
        else if(scrollNow <= maxScrollAmount) {
            scrollNow = maxScrollAmount;
        }

        int offset = borderOffset + scrollNow;

        MatrixStack matrices = context.getMatrices();

       // guiUtils.drawRect(context, posX + (35 * sf), posY + (55 * sf), posX + ((width / 2) * 1) + (60 * sf), posY + ((85 + 170) * sf), new Color(0, 0, 0, 100).getRGB());

        matrices.push();
        RenderUtils.enableScissor((posX + (35 * sf)) / sf, (posY + (55 * sf) + borderOffset) / sf, (posX + ((width / 2) * 1) + (60 * sf)) / sf, (posY + ((85 + 170) * sf) - borderOffset) / sf);
      //  guiUtils.drawRect(context, 0, 0, window.getWidth(), window.getHeight(), -1);

        for (Animation3ValueComp comp : comps) {
            comp.render(context, mouseX, mouseY, posX + (45 * sf), frameTop + offset, 240 * sf, 20 * sf);
            offset += compHeight + compOffset;
        }

        RenderUtils.disableScissor();
        matrices.pop();

        if(back == null) {
            back = new Button("Back", new Color(97, 187, 95));
            back.addClickListener(() -> {
                parent.setViewMode(ViewMode.Import);
                parent.closeSettings();
            });
        }

        back.render(context, mouseX, mouseY, posX + (15 * sf), posY + parent.height - (25 * sf), 50 * sf, 20 * sf);

      //  guiUtils.drawRoundedRect(context, posX + (width / 2) + (25 * sf), posY + (210 * sf), posX + (width / 2) + (60 * sf), posY + (245 * sf), 1, Color.red.getRGB());
        guiUtils.drawScaledImage(context, posX + (width / 2) + (25 * sf), posY + (210 * sf), 0.15f, "plus.png");

        if(editing != null) {

            
            FontUtil.normal.drawString(context, "Editing " + editing.getName(), settingsX + (7 * sf), settingsY + (40 * sf), -1);
            

            editingNameField.render(context, mouseX, mouseY, settingsX + (10 * sf), settingsY + (60 * sf), settingsWidth - (20 * sf), 25 * sf);
            editingType.render(context, mouseX, mouseY, settingsX + (10 * sf), settingsY + (90 * sf), settingsWidth - (20 * sf), 25 * sf);
            toggleButton.render(context, mouseX, mouseY, settingsX + (10 * sf), settingsY + editingType.getCustomHeight() + (95 * sf), settingsWidth - (20 * sf), 25 * sf);

            editing.setName(editingNameField.getText());
            editing.setReverse(toggleButton.isEnabled());
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

        int sf = guiUtils.getGuiScale();

        double posX = parent.posX, posY = parent.posY, width = parent.width;
        if(isInside(mouseX, mouseY, posX + (width / 2) + (25 * sf), posY + (210 * sf), posX + (width / 2) + (60 * sf), posY + (245 * sf))) {

            Custom3ValueAnimation animation = new Custom3ValueAnimation("Translate Animation");
            CustomAnimation xAnim = new CustomAnimation(animation);
            xAnim.setAnimationType(CustomAnimation.AnimationType.TranslateX);

            CustomAnimation yAnim = new CustomAnimation(animation);
            yAnim.setAnimationType(CustomAnimation.AnimationType.TranslateY);

            CustomAnimation zAnim = new CustomAnimation(animation);
            zAnim.setAnimationType(CustomAnimation.AnimationType.TranslateZ);

            animation.setxAnimation(xAnim);
            animation.setyAnimation(yAnim);
            animation.setzAnimation(zAnim);

            comps.add(new Animation3ValueComp(this, animation));
            cosmetic.addCustomAnimation(animation);
        }

        back.onClick(mouseX, mouseY, button);
        try {
            for (Animation3ValueComp comp : comps) {
                comp.onClick(mouseX, mouseY, button);
            }
        }
        catch (ConcurrentModificationException ignored) {}

        editComp.onClick(mouseX, mouseY, button);

        if(editing != null) {
            editingNameField.onClick(mouseX, mouseY, button);
            editingType.onClick(mouseX, mouseY, button);
            toggleButton.onClick(mouseX, mouseY, button);
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY, int button) {
        editComp.onRelease(mouseX, mouseY, button);
        for(Animation3ValueComp comp : comps) {
            comp.onRelease(mouseX, mouseY, button);
        }

        if(editing != null) {
            editingNameField.onRelease(mouseX, mouseY, button);
            editingType.onRelease(mouseX, mouseY, button);
        }
    }

    @Override
    public void handleMouseInput(double dWheel) throws IOException {
        int sf = guiUtils.getGuiScale();
        double posX = parent.posX, posY = parent.posY, width = parent.width;
        if(isInside(mouseX, mouseY, posX + (35 * sf), posY + (55 * sf), posX + ((width / 2) * 1) + (60 * sf), posY + ((85 + 170) * sf))) {

            // Scroll up
            if(dWheel < 0) {
                scrollNow -= 7;
            }
            // Scroll down
            else if(dWheel > 0) {
                scrollNow += 7;
            }
        }

        editComp.handleMouseInput(dWheel);
        for(Animation3ValueComp comp : comps) {
            comp.handleMouseInput(dWheel);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        editComp.keyTyped(typedChar, keyCode);
        for(Animation3ValueComp comp : comps) {
            comp.keyTyped(typedChar, keyCode);
        }

        if(editing != null) {
            editingNameField.keyTyped(typedChar, keyCode);
            editingType.keyTyped(typedChar, keyCode);
        }
    }

    public void setCosmetic(CustomCosmetic cosmetic) {
        comps.clear();
        this.cosmetic = cosmetic;
        for (Custom3ValueAnimation customAnimation : cosmetic.getCustomAnimations()) {
            comps.add(new Animation3ValueComp(this, customAnimation));
        }
        editing = null;
        editComp.setCustomAnimation(null);
    }

    public CustomCosmetic getCosmetic() {
        return cosmetic;
    }

    public void removeAnimation(Custom3ValueAnimation animation, Animation3ValueComp comp) {
        cosmetic.removeCustomAnimation(animation);
        comps.remove(comp);
        editing = null;
        editComp.setCustomAnimation(null);
        parent.closeSettings();
    }

    public void edit(@Nullable Custom3ValueAnimation animation) {
        this.editing = animation;

        if(animation == null) return;

        this.editingNameField = new TextField(animation.getName());

        editingType = new DropDown("Type", new Color(126, 240, 124), Custom3ValueAnimation.Type.getValues());
        editingType.setSelectedOption(animation.getType().name());
        editingType.addValueChangeListener((oldValue, newValue) -> animation.setType(Custom3ValueAnimation.Type.valueOf(newValue)));

        toggleButton = new ToggleButton("Reverse", animation.shouldReverse(), new Color(126, 240, 124));
    }

    public AnimationEditComp getEditComp() {
        return editComp;
    }

    public MainScreen getParent() {
        return parent;
    }
}
