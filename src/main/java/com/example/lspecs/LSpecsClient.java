package com.example.lspecs;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class LSpecsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("lspecs")
                .then(literal("gpu")
                    .then(argument("text", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String value = StringArgumentType.getString(ctx, "text");
                            SpecOverrides.gpu = value;
                            ctx.getSource().sendFeedback(
                                Text.literal("LSpecs: GPU line will now show \"" + value + "\" (open F3 to check)"));
                            return 1;
                        })))
                .then(literal("cpu")
                    .then(argument("text", StringArgumentType.greedyString())
                        .executes(ctx -> {
                            String value = StringArgumentType.getString(ctx, "text");
                            SpecOverrides.cpu = value;
                            ctx.getSource().sendFeedback(
                                Text.literal("LSpecs: CPU line will now show \"" + value + "\" (open F3 to check)"));
                            return 1;
                        })))
                .then(literal("reset")
                    .executes(ctx -> {
                        SpecOverrides.cpu = null;
                        SpecOverrides.gpu = null;
                        ctx.getSource().sendFeedback(Text.literal("LSpecs: reset to your real specs"));
                        return 1;
                    }))
            )
        );
    }
}
