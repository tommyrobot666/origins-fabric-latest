package io.github.apace100.origins.command.argument;

import io.github.apace100.origins.Origins;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;

public class ModArgumentTypes {
    public static void register(){
        ArgumentTypeRegistry.registerArgumentType(
                Origins.identifier("origin"),
                OriginArgumentType.class,
                SingletonArgumentInfo.contextFree(OriginArgumentType::new)
        );
        ArgumentTypeRegistry.registerArgumentType(
                Origins.identifier("layer"),
                OriginArgumentType.class,
                SingletonArgumentInfo.contextFree(OriginArgumentType::new)
        );
    }
}
