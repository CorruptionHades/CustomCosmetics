package me.corruptionhades.customcosmetics.ui;

import me.corruptionhades.customcosmetics.animation.Animation;
import me.corruptionhades.customcosmetics.animation.Easing;
import me.corruptionhades.customcosmetics.cosmetic.BodyPart;
import me.corruptionhades.customcosmetics.cosmetic.custom.CustomCosmetic;
import me.corruptionhades.customcosmetics.interfaces.IMinecraftInstance;
import me.corruptionhades.customcosmetics.objfile.ObjFile;
import me.corruptionhades.customcosmetics.ui.comp.SettingsView;
import me.corruptionhades.customcosmetics.ui.comp.impl.Button;
import me.corruptionhades.customcosmetics.ui.comp.impl.DropDown;
import me.corruptionhades.customcosmetics.ui.comp.impl.TextField;
import me.corruptionhades.customcosmetics.utils.FontUtil;
import me.corruptionhades.customcosmetics.utils.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public class MainScreen extends Screen implements IMinecraftInstance {

    public static boolean isOpen;
    private final boolean editMode;

    private ViewMode viewMode;
    private CreateScreen customAnimationScreen;

    public MainScreen() {
        super(Text.of("Custom Cosmetics Screen"));
        isOpen = true;
        editMode = false;
        viewMode = ViewMode.Import;
    }

    public MainScreen(CustomCosmetic customCosmetic) {
        super(Text.of("Custom Cosmetics Screen"));
        isOpen = true;
        this.customCosmetic = customCosmetic;
        editMode = true;
        viewMode = ViewMode.Import;
    }

    public GuiUtils guiUtils = new GuiUtils();

    public double posX, posY, width, height, dragX, dragY;
    public boolean dragging;

    public Color lGreen = new Color(126, 240, 124);
    public Color dGreen = new Color(97, 187, 95);

    private Button importModel, importTexture;
    private SettingsView settingsView;

    private float modelRotateX = 0;
    private boolean rotating = false;
    private boolean autoRotate = false;

    private String infoText;

    private CustomCosmetic customCosmetic;
    private TextField nameField;

    private Animation expandAnimation;

    private DropDown bodyPart;
    private DropDown importMode;

    private boolean expanded;

    private boolean leftMouseDown = false;

    @Override
    protected void init() {
        super.init();

        int sf = guiUtils.getGuiScale();

        customAnimationScreen = new CreateScreen(this);
        dragging = false;

        viewMode = ViewMode.Import;

        posX = 150;
        posY = 100;
        width = 600 * sf;
        height = 300 * sf;

        nameField = new TextField(editMode ? customCosmetic.getName() : "Enter Name");
        if(editMode) nameField.setEditable(false);

        importModel = new Button("Import Obj Model: shield.obj", lGreen);
        if(editMode) importModel.setClickable(false);
        importModel.addClickListener(() -> {
            try {

                if(!nameField.isChanged()) {
                    infoText = "Please enter a name!";
                    return;
                }

                infoText = "Importing Model...";
                //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                customCosmetic = new CustomCosmetic(
                        nameField.getText(), BodyPart.valueOf(bodyPart.getSelectedOption().toUpperCase().replace(" ", "_")), false);

                File path = new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/Lightning Shield Blue/roundshield.obj");
                System.out.println(path.getAbsolutePath());

                customCosmetic.setModel(new ObjFile("model.obj", ObjFile.ResourceProvider.ofPath(Path.of("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/Lightning Shield Blue"))));
                customCosmetic.setModelFile(path);
                customCosmetic.setColor(new Color(255, 255, 255, 255));
                customCosmetic.setScaleX(0.068F);
                customCosmetic.setScaleY(0.068F);
                customCosmetic.setScaleZ(0.068F);

                customCosmetic.setRotX(0);
                customCosmetic.setRotY(0);
                customCosmetic.setRotZ(0);
              //  CosmeticsManager.registerCosmetics(customCosmetic);

                settingsView.setCosmetic(customCosmetic);
                infoText = "Imported Model!";
            }
            catch (Exception e) {
                e.printStackTrace();
                infoText = "Failed to Import Model!";
            }
        });

        importMode = new DropDown("Import Mode", lGreen, "Single image", "Animated");

        importTexture = new Button("Import Texture", lGreen);
        importTexture.addClickListener(() -> {
            infoText = "Importing Texture...";
            try {
                //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

           /*     switch (importMode.getSelectedOption()) {
                    case "Single image" -> importSingleTexture();
                    case "Animated" -> importAnimatedTexture();
                } */

                infoText = "Imported Texture!";

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        settingsView = new SettingsView(this, customCosmetic);
        isOpen = true;

        expanded = true;
        expandAnimation = new Animation(500, 0, -140 * sf, Easing.EASE_OUT_CUBIC);

        // convert enum to string array

        bodyPart = new DropDown("Body Part", lGreen, BodyPart.bodyParts());
        bodyPart.addValueChangeListener(((oldVal, newVal) -> {
            if(customCosmetic != null) {
                customCosmetic.setBodyPart(BodyPart.valueOf(newVal.toUpperCase()));
            }
        }));
        if(customCosmetic != null) bodyPart.setSelectedOption(customCosmetic.getType().name().toUpperCase());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int sf = guiUtils.getGuiScale();

        if (dragging) {
            float xDiff = (float) (mouseX - dragX);
            float yDiff = (float) (mouseY - dragY);
            posX = xDiff * sf;
            posY = yDiff * sf;
        }

        // Sidebar
        guiUtils.drawRoundedRect(context, posX + expandAnimation.getValue(), posY, posX + (30 * sf), posY + height, 35, dGreen.getRGB());
        guiUtils.drawRoundedOutline(context, posX + expandAnimation.getValue(), posY, posX + (30 * sf), posY + height, 35, 4, lGreen.getRGB(), dGreen.getRGB());

        if(viewMode == ViewMode.Import) {
            nameField.render(context, mouseX, mouseY, posX + expandAnimation.getValue() + (20 * sf),
                    posY + (50 * sf),
                    100 * sf, 25 * sf);

            bodyPart.render(context, mouseX, mouseY, posX + expandAnimation.getValue() + (20 * sf),
                    posY + (60 * sf) + nameField.getHeight(),
                    110 * sf, 25 * sf);

            importModel.render(context, mouseX, mouseY, posX + expandAnimation.getValue() + (20 * sf),
                    posY + (70 * sf) + nameField.getHeight() + bodyPart.getCustomHeight(),
                    100 * sf, 25 * sf);

            importMode.render(context, mouseX, mouseY, posX + expandAnimation.getValue() + (20 * sf),
                    posY + (80 * sf) + nameField.getHeight() + (bodyPart.getCustomHeight()) + importModel.getHeight(),
                    100 * sf, 25 * sf);

            importTexture.render(context, mouseX, mouseY, posX + expandAnimation.getValue() + (20 * sf),
                    posY + (90 * sf) + nameField.getHeight() + (bodyPart.getCustomHeight()) + importModel.getHeight() + importMode.getCustomHeight(),
                    100 * sf, 25 * sf);
        }

        // Main
        guiUtils.drawRoundedRect(context, posX, posY, posX + width, posY + height, 35, lGreen.getRGB());
        guiUtils.drawRoundedOutline(context, posX, posY, posX + width, posY + height, 35, 4, dGreen.getRGB(), lGreen.getRGB());

        // Player

        int playerX = (int) (posX + (485 * sf));
        int offset = (90 * sf);
        guiUtils.drawRoundedRect(context, playerX - offset, posY + (45 * sf), playerX + offset, posY + height - (30 * sf), 20, dGreen.getRGB());

      //  RenderUtils.drawEntityOnScreen(playerX, (int) posY + 260, 90, modelRotateX, 180, mc.thePlayer);
        InventoryScreen.drawEntity(context, (int) ((posX + 485 * sf) / sf), (int) ((posY + 260 * sf) / sf), (int) ((posX + 485 * sf) / sf), (int) ((posY + 260 * sf) / sf), 90, 0,
                0, 0,
                mc.player);

        guiUtils.drawScaledImage(context, playerX + offset - (30 * sf), posY + (49 * sf), 0.05F, autoRotate ? "play.png" : "pause.png");

        //guiUtils.drawRect(playerX - offset, posY + 73, playerX + offset, posY + height - 30, Color.red.getRGB());

        if (leftMouseDown && rotating) {
            float mouseOffset = (float) (mouseX - (playerX + offset));
            modelRotateX = mouseOffset * 2;
        } else if (!rotating && autoRotate) {
            modelRotateX += 0.1;
        }
        

        if(viewMode == ViewMode.Import) {

            // --------------------------------------------------------------------------------------------------------------------------------------------------

            float imageX = (float) (posX + (10 * sf));
            float imageY = (float) (posY + height - (35 * sf));

            guiUtils.drawScaledImage(context, imageX, imageY, 0.6F, "settings.png");

            // --------------------------------------------------------------------------------------------------------------------------------------------------
            double stringWidth = FontUtil.big.getStringWidth("Import Custom cosmetics");
            double middle = ((width) / 2) - (stringWidth / 2);
            FontUtil.big.drawString(context, "Import Custom cosmetics", posX + (int) middle, (int) posY + (10 * sf), -1);

            settingsView.render(context, mouseX, mouseY, posX + (35 * sf), posY + (85 * sf), 200, 150);
            
            FontUtil.normal.drawString(context, infoText, posX + (45 * sf), posY + height - (10 * sf) - FontUtil.normal.getHeight(), -1);
        }
        else if(viewMode == ViewMode.Animation) {
            customAnimationScreen.render(context, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

        int sf = guiUtils.getGuiScale();

        if (isInside(mouseX, mouseY, posX, posY, posX + width, posY + (15 * sf)) && mouseButton == 0) {
            dragging = true;
            dragX = (int) (mouseX - posX / sf);
            dragY = (int) (mouseY - posY / sf);
        }

        if(expanded) {

            if(viewMode == ViewMode.Import) {
                importModel.onClick(mouseX, mouseY, mouseButton);
                importTexture.onClick(mouseX, mouseY, mouseButton);
                nameField.onClick(mouseX, mouseY, mouseButton);
                bodyPart.onClick(mouseX, mouseY, mouseButton);
                importMode.onClick(mouseX, mouseY, mouseButton);
            }
        }
        if(viewMode == ViewMode.Import) {
            if(isInside(mouseX, mouseY, posX + (10 * sf), posY + height - (30 * sf), posX + (40 * sf), posY + height - (5 * sf))) {
                toggleSettings();
            }
            settingsView.onClick(mouseX, mouseY, mouseButton);
        }
        else if(viewMode == ViewMode.Animation) {
            customAnimationScreen.onClick(mouseX, mouseY, mouseButton);
        }

        int playerX = (int) (posX + (485 * sf));
        int offset = 90 * sf;
        if(isInside(mouseX, mouseY, playerX - offset, posY + (73 * sf), playerX + offset, posY + height - (30 * sf)) && mouseButton == 0) {
            rotating = true;
            leftMouseDown = true;
            autoRotate = false;
        }
        else {
            rotating = false;
        }

        if(isInside(mouseX, mouseY, playerX + offset - (30 * sf), posY + (49 * sf), playerX + offset - (4 * sf), posY + (75 * sf)) && mouseButton == 0) {
            autoRotate = !autoRotate;
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        dragging = false;
        rotating = false;
        leftMouseDown = false;

        if(viewMode == ViewMode.Import) {
            settingsView.onRelease(mouseX, mouseY, button);
            nameField.onRelease(mouseX, mouseY, button);
            bodyPart.onRelease(mouseX, mouseY, button);
            importMode.onRelease(mouseX, mouseY, button);
        }
        else if(viewMode == ViewMode.Animation) {
            customAnimationScreen.onRelease(mouseX, mouseY, button);
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        char typedChar = (char) keyCode;
        typedChar = Character.toString(typedChar).toLowerCase(Locale.ROOT).charAt(0);

        if(InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            typedChar = Character.toString(typedChar).toUpperCase(Locale.ROOT).charAt(0);
        }

        if(viewMode == ViewMode.Import) nameField.keyTyped(typedChar, keyCode);
        else if(viewMode == ViewMode.Animation) customAnimationScreen.keyTyped(typedChar, keyCode);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isInside(double mouseX, double mouseY, double x, double y, double x2, double y2) {
        double sf = MinecraftClient.getInstance().getWindow().getScaleFactor();
        x /= sf;
        y = y / sf;
        x2 = x2 / sf;
        y2 = y2 / sf;

        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    @Override
    public void close() {
        super.close();
        isOpen = false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        try {
            customAnimationScreen.handleMouseInput(verticalAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

  /*  private void importSingleTexture() {
        File selectedFile = new File("X:/rs.png");
        BufferedImage image = null;
        try {
            image = ImageIO.read(selectedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureId = GL11.glGenTextures();
        // Bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        // Upload the image data to the texture
        TextureUtil.uploadTextureImage(textureId, image);
        // Unbind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        customCosmetic.setTextureFile(selectedFile);
        if(image != null) {
            ResourceLocation resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("texture", new DynamicTexture(image));
            CustomResourceLocation customResourceLocation = new CustomResourceLocation(false, resourceLocation);
            customCosmetic.setTextureLocation(customResourceLocation);
            customCosmetic.setTextureFile(selectedFile);
        }
    }

    private void importAnimatedTexture() {
        File folder = new File("H:/C#/BadlionCosmetic/bin/Debug/net7.0/out/shield/Lightning Shield Blue/frames");

        if(!folder.exists()) return;

        File[] files = folder.listFiles();
        if(files == null) return;
        if(files.length == 0) return;

        List<ResourceLocation> locations = new ArrayList<>();
        for (File file : files) {
            BufferedImage image;
            try {
                image = ImageIO.read(file);
                if(image == null) continue;
                int textureId = GL11.glGenTextures();
                // Bind the texture
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                // Upload the image data to the texture
                TextureUtil.uploadTextureImage(textureId, image);
                // Unbind the texture
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                // Create the resource location
                ResourceLocation resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("texture", new DynamicTexture(image));
                // Add the resource location to the list
                locations.add(resourceLocation);
            }
            catch (IOException ignored) {}
        }

        customCosmetic.setTextureFile(folder);
        CustomResourceLocation customResourceLocation = new CustomResourceLocation(true, locations.toArray(new ResourceLocation[0]));
        customCosmetic.setTextureLocation(customResourceLocation);
    } */

    public void toggleSettings() {
        expanded = !expanded;
        if(expandAnimation.isDone()) {
            if(expandAnimation.getEnd() == 0) {
                openSettings();
            }
            else {
                closeSettings();
            }
        }
    }

    public void closeSettings() {
        expanded = false;
        if(expandAnimation.isDone() && expandAnimation.getEnd() != 0) {
            expandAnimation.setEnd(0);
        }
    }

    public void openSettings() {
        int sf = guiUtils.getGuiScale();
        expanded = true;
        if(expandAnimation.isDone() && expandAnimation.getEnd() != (-140 * sf)) {
            expandAnimation.setEnd(-140 * sf);
        }
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
    }

    public CustomCosmetic getCustomCosmetic() {
        return customCosmetic;
    }

    public CreateScreen getCustomAnimationScreen() {
        return customAnimationScreen;
    }

    public double getSettingsX() {
        return posX + expandAnimation.getValue();
    }

    public double getSettingsY() {
        return posY;
    }
}
