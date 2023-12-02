package org.bsrserver.altair.defaultcredentials;

public record Credential(
        boolean isDefault,
        boolean isGot,
        String username,
        String password
) {
}
