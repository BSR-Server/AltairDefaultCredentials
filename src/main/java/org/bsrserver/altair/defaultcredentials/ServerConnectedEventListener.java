package org.bsrserver.altair.defaultcredentials;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;
import java.util.UUID;

public class ServerConnectedEventListener {
    private final AltairDefaultCredentials altairDefaultCredentials;

    public ServerConnectedEventListener(AltairDefaultCredentials altairDefaultCredentials) {
        this.altairDefaultCredentials = altairDefaultCredentials;
    }

    @Subscribe(order = PostOrder.LATE)
    public void onServerConnectedEvent(ServerConnectedEvent event) {
        if (event.getPreviousServer().isEmpty()) {
            UUID uuid = event.getPlayer().getUniqueId();
            Optional<Credential> credential = altairDefaultCredentials.getData().getCredential(uuid);

            // return if credential is not present
            if (credential.isEmpty()) {
                return;
            }

            // send message
            if (!credential.get().isGot()) {
                // commands
                String GET_COMMAND = "/altairdefaultcredentials get";
                String AVOID_COMMAND = "/altairdefaultcredentials avoidWelcomeMessage";

                // message
                Component message = Component.text("-".repeat(40))
                        .append(Component.text("\n"))
                        .append(
                                Component
                                        .text("[IMPORTANT] ")
                                        .color(NamedTextColor.GREEN)
                        )
                        .append(
                                Component
                                        .text("BSR Website is now online!")
                                        .color(NamedTextColor.WHITE)
                        )
                        .append(Component.text("\n"))
                        .append(
                                Component
                                        .text("Click")
                                        .color(NamedTextColor.WHITE)
                        )
                        .append(
                                Component
                                        .text(" [here] ")
                                        .color(NamedTextColor.GREEN)
                                        .hoverEvent(Component.text(GET_COMMAND))
                                        .clickEvent(ClickEvent.runCommand(GET_COMMAND))
                        )
                        .append(
                                Component
                                        .text("to show your default credential and website link.")
                                        .color(NamedTextColor.WHITE)
                        )
                        .append(Component.text("\n"))
                        .append(
                                Component
                                        .text("Click")
                                        .color(NamedTextColor.WHITE)
                        )
                        .append(
                                Component
                                        .text(" [here] ")
                                        .color(NamedTextColor.GREEN)
                                        .hoverEvent(Component.text(AVOID_COMMAND))
                                        .clickEvent(ClickEvent.runCommand(AVOID_COMMAND))
                        )
                        .append(
                                Component
                                        .text("to avoid always displaying this message at login.")
                                        .color(NamedTextColor.WHITE)
                        )
                        .append(Component.text("\n"))
                        .append(Component.text("-".repeat(40)))
                        .append(Component.text("\n"));

                // send message to player
                event.getPlayer().sendMessage(message);
            }
        }
    }
}
