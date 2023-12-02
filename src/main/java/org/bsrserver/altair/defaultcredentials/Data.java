package org.bsrserver.altair.defaultcredentials;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.Optional;
import java.nio.file.Path;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.velocitypowered.api.proxy.Player;

public class Data {
    private final AltairDefaultCredentials altairDefaultCredentials;
    private final File dataFile;
    private JSONObject users;

    public Data(AltairDefaultCredentials plugin) {
        this.altairDefaultCredentials = plugin;
        Logger logger = plugin.getLogger();
        Path dataDirectory = plugin.getDataDirectory();

        // check if data directory exists
        if (!dataDirectory.toFile().exists()) {
            logger.error("Created data directory: " + dataDirectory + ", please put data files in it.");
            dataDirectory.toFile().mkdir();
        }

        // load data files
        dataFile = new File(dataDirectory.toAbsolutePath().toString(), "users.json");
        if (!dataFile.exists()) {
            logger.error("Cannot find data file: " + dataFile.getAbsolutePath());
            return;
        }

        // read data files
        try {
            users = JSON.parseObject(dataFile.toURI().toURL());
        } catch (MalformedURLException ignored) {
        }
    }

    private void saveData() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile);
            JSON.writeTo(fileOutputStream, users, JSONWriter.Feature.PrettyFormat);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUsername(UUID uuid) {
        return altairDefaultCredentials.getProxyServer().getPlayer(uuid).map(Player::getUsername).orElse("Unknown");
    }

    public Optional<Credential> getCredential(UUID uuid) {
        if (users == null) {
            return Optional.empty();
        }

        JSONObject user = users.getJSONObject(uuid.toString());
        if (user != null) {
            return Optional.of(new Credential(
                    false,
                    user.getBoolean("got_password"),
                    user.getString("username"),
                    user.getString("password")
            ));
        } else {
            return Optional.of(new Credential(
                    true,
                    false,
                    getUsername(uuid),
                    AltairDefaultCredentials.DEFAULT_PASSWORD
            ));
        }
    }

    public void setGot(UUID uuid) {
        if (users != null) {
            JSONObject user = users.getJSONObject(uuid.toString());
            if (user == null) {
                user = new JSONObject();
                user.put("uuid", uuid.toString());
                user.put("username", getUsername(uuid));
                user.put("password", AltairDefaultCredentials.DEFAULT_PASSWORD);
                users.put(uuid.toString(), user);
            }

            // set and save
            user.put("got_password", true);
            saveData();
        }
    }
}
