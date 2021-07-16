package dev.maniac.xpUtilities;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class yeet {
    public static boolean debuggerReleaseControl() {
        GLFW.glfwSetInputMode(MinecraftClient.getInstance().getWindow().getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        return true;
    }
}
