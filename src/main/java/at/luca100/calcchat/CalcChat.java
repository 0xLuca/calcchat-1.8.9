package at.luca100.calcchat;

import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.mariuszgromada.math.mxparser.Expression;

import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public final class CalcChat extends LabyModAddon {
    private final Minecraft mc;
    private Field textFieldField = null;

    public CalcChat() {
        for (Field field : GuiChat.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType().equals(GuiTextField.class)) {
                textFieldField = field;
                break;
            }
            field.setAccessible(false);
        }
        if (textFieldField == null) {
            throw new RuntimeException("Could not find the GuiTextField in GuiChat");
        }
        mc = Minecraft.getMinecraft();
    }

    @Override
    public void onEnable() {
        getApi().registerForgeListener(new ClientTickListener(this));
    }

    public void handleTickEvent() {
        if (mc.currentScreen instanceof GuiChat && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_C)) {
            final GuiTextField textField;
            try {
                textField = (GuiTextField) textFieldField.get(mc.currentScreen);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
            if (!textField.getText().isEmpty() && textField.getSelectedText().isEmpty()) {
                final String content = textField.getText();
                final StringBuilder newContent = new StringBuilder();
                Arrays.stream(content.split(" ")).forEach(word -> {
                    String result = String.valueOf(new Expression(word).calculate());
                    if (result.equals("NaN")) {
                        newContent.append(word);
                    } else {
                        if (result.endsWith(".0")) {
                            result = result.substring(0, result.length() - 2);
                        }
                        newContent.append(result);
                    }
                    newContent.append(" ");
                });
                textField.setText(newContent.toString().trim());
            }
        }
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

    }
}
