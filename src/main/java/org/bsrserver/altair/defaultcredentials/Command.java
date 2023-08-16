package org.bsrserver.altair.defaultcredentials;

import java.util.UUID;
import java.util.Optional;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public class Command implements SimpleCommand {
    private final Data data;

    public Command(AltairDefaultCredentials altairDefaultCredentials) {
        data = altairDefaultCredentials.getData();
    }

    private Component getCopiedMessage(String text) {
        return Component.text("[%s]".formatted(text))
                .color(NamedTextColor.GREEN)
                .hoverEvent(Component.text("Click to copy"))
                .clickEvent(ClickEvent.copyToClipboard(text));
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (source instanceof Player) {
            UUID uuid = ((Player) source).getUniqueId();
            Optional<Credential> credential = data.getCredential(uuid);

            if (credential.isPresent()) {
                source.sendMessage(
                        Component.text("Your default credential is: ")
                                .append(Component.text("\n"))
                                .append(Component.text("Username: "))
                                .append(getCopiedMessage(credential.get().username()))
                                .append(Component.text("\n"))
                                .append(Component.text("Password: "))
                                .append(getCopiedMessage(credential.get().password()))
                );
            } else {
                source.sendMessage(Component.text("You don't have a default credential."));
            }
        } else {
            source.sendMessage(Component.text("This command can only be executed by players."));
        }
    }
}
