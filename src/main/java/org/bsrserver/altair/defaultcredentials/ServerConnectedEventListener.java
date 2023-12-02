package org.bsrserver.altair.defaultcredentials;

import java.util.UUID;
import java.util.Optional;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;

public class ServerConnectedEventListener {
    private final AltairDefaultCredentials altairDefaultCredentials;

    public ServerConnectedEventListener(AltairDefaultCredentials altairDefaultCredentials) {
        this.altairDefaultCredentials = altairDefaultCredentials;
    }

    @Subscribe
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
                String COMMAND = "/altairdefaultcredentials avoidWelcomeMessage";
                event.getPlayer().sendMessage(MessagesUtil.getCredentialMessage(credential.get()));
                event.getPlayer().sendMessage(
                        Component
                                .text("Click")
                                .color(NamedTextColor.GRAY)
                                .append(
                                        Component
                                                .text(" [here] ")
                                                .color(NamedTextColor.GREEN)
                                                .hoverEvent(Component.text(COMMAND))
                                                .clickEvent(ClickEvent.runCommand(COMMAND))
                                )
                                .append(
                                        Component
                                                .text("to avoid always displaying this message at login.")
                                                .color(NamedTextColor.GRAY)
                                )
                );
            }
        }
    }
}
