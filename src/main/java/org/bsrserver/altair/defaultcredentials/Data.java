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

public class Data {
    private final File dataFile;
    private JSONObject users;

    public Data(AltairDefaultCredentials plugin) {
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

    public Optional<Credential> getCredential(UUID uuid) {
        if (users == null) {
            return Optional.empty();
        }

        JSONObject user = users.getJSONObject(uuid.toString());
        if (user != null) {
            user.put("got_password", true);
            saveData();
            return Optional.of(new Credential(user.getString("username"), user.getString("password")));
        } else {
            return Optional.empty();
        }
    }
}
