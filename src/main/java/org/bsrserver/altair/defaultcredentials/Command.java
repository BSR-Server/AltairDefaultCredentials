package org.bsrserver.altair.defaultcredentials;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Stream;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public class Command implements SimpleCommand {
    private final Data data;

    public Command(AltairDefaultCredentials altairDefaultCredentials) {
        data = altairDefaultCredentials.getData();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        // execute
        if (source instanceof Player) {
            UUID uuid = ((Player) source).getUniqueId();

            // branch
            if (args.length == 1 && args[0].equals("get")) {
                Optional<Credential> credential = data.getCredential(uuid);

                if (credential.isPresent()) {
                    source.sendMessage(MessagesUtil.getCredentialMessage(credential.get()));
                    data.setGot(uuid);
                } else {
                    source.sendMessage(
                            Component
                                    .text("Error getting credential, please contact the server administrator.")
                                    .color(NamedTextColor.RED)
                    );
                }
            } else if (args.length == 1 && args[0].equals("avoidWelcomeMessage")) {
                data.setGot(uuid);
                source.sendMessage(
                        Component
                                .text("You will no longer receive this message at login.")
                                .color(NamedTextColor.GREEN)
                );
            } else {
                source.sendMessage(Component.text("Error command").color(NamedTextColor.RED));
            }
        } else {
            source.sendMessage(Component.text("This command can only be executed by players."));
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        // return empty list if source is player
        if (source instanceof Player) {
            return List.of();
        }

        // suggests
        if (args.length <= 1) {
            return Stream.of("get", "avoidWelcomeMessage")
                    .filter(s -> s.startsWith(args.length > 0 ? args[0] : ""))
                    .toList();
        } else {
            return List.of();
        }
    }
}
