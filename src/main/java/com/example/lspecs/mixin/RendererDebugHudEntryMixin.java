package com.example.lspecs.mixin;

import com.example.lspecs.SpecOverrides;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.gui.hud.debug.RendererDebugHudEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererDebugHudEntry.class)
public class RendererDebugHudEntryMixin {

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/debug/DebugHudLines;addLine(Ljava/lang/String;)V"
        ),
        require = 0
    )
    private void lspecs$redirectAddLine(DebugHudLines lines, String line) {
        lines.addLine(applyOverride(line));
    }

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/debug/DebugHudLines;addLineToSection(Lnet/minecraft/util/Identifier;Ljava/lang/String;)V"
        ),
        require = 0
    )
    private void lspecs$redirectAddLineToSection(DebugHudLines lines, Identifier section, String line) {
        lines.addLineToSection(section, applyOverride(line));
    }

    private static String applyOverride(String line) {
        String override = SpecOverrides.gpu;
        if (override == null) {
            return line;
        }
        String lower = line.toLowerCase();
        if (lower.contains("renderer") || lower.contains("gpu") || lower.contains("gl_renderer")) {
            int colon = line.indexOf(':');
            String label = colon >= 0 ? line.substring(0, colon + 1) : "GPU:";
            return label + " " + override;
        }
        return line;
    }
}
