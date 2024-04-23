package org.bsrserver.altair.defaultcredentials;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessagesUtil {
    private static final String GLOBAL_URL = "https://www.bsrserver.org:8443/login";
    private static final String CHINA_URL = "https://www.bsrserver.org.cn:8443/login";

    private static Component getCopiableMessage(String text) {
        return Component.text("[%s]".formatted(text))
                .color(NamedTextColor.GREEN)
                .hoverEvent(Component.text("Click to copy"))
                .clickEvent(ClickEvent.copyToClipboard(text));
    }

    public static Component getCredentialMessage(Credential credential) {
        Component message = Component.empty();

        // default warning
        if (credential.isDefault()) {
            message = message.append(
                    Component.text(
                                    "Cannot find your default credential in the database, " +
                                            "please try following credential or contact server administrators.\n"
                            )
                            .color(NamedTextColor.YELLOW)
            );
        }

        // append credential
        message = message
                .append(Component.text("Your default altair credential is: "))
                .append(Component.text("\n"))
                .append(Component.text("Username: "))
                .append(getCopiableMessage(credential.username()))
                .append(Component.text("\n"))
                .append(Component.text("Password: "))
                .append(getCopiableMessage(credential.password()))
                .append(Component.text("\n"));

        // Login to Altair
        message = message
                .append(
                        Component
                                .text("Click to visit the website: ")
                                .color(NamedTextColor.WHITE)
                )
                .append(
                        Component
                                .text("[GLOBAL]")
                                .color(NamedTextColor.GREEN)
                                .hoverEvent(Component.text("Click to visit " + GLOBAL_URL))
                                .clickEvent(ClickEvent.openUrl(GLOBAL_URL))
                )
                .append(
                        Component
                                .text(" and ")
                                .color(NamedTextColor.WHITE)
                )
                .append(
                        Component
                                .text("[CHINA]")
                                .color(NamedTextColor.GREEN)
                                .hoverEvent(Component.text("Click to visit " + CHINA_URL))
                                .clickEvent(ClickEvent.openUrl(CHINA_URL))
                );

        // return message
        return message;
    }
}
