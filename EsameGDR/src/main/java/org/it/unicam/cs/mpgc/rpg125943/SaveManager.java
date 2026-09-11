package org.it.unicam.cs.mpgc.rpg125943;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

    private static final Path SAVE_DIR = Path.of("Saves");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void save(String saveName, Player player, List<EnemyData> enemies, int enemyIndex, boolean fightBoss, EnemyData boss) {
        GameSaveData data = new GameSaveData(PlayerData.fromPlayer(player), enemies, enemyIndex, fightBoss, boss);
        try  {
            Files.createDirectories(SAVE_DIR);
            Files.writeString(fileFor(saveName), GSON.toJson(data));
        } catch (IOException e) {
            throw new RuntimeException("Salvataggio fallito: " + e.getMessage(), e);
        }
    }
    public GameSaveData load(String saveName) {
        try {
            String json = Files.readString(fileFor(saveName));
            return GSON.fromJson(json, GameSaveData.class);
        } catch (IOException e) {
            throw new RuntimeException("Caricamento fallito: " + e.getMessage(), e);
        }
    }

    public void delete(String saveName) {
        try {
            Files.deleteIfExists(fileFor(saveName));
        } catch (IOException e) {
            throw new RuntimeException("Eliminazione salvataggio fallita: " + e.getMessage(), e);
        }
    }

    public List<String> listSaves() {
        List<String> names = new ArrayList<>();
        if (!Files.exists(SAVE_DIR)) {
            return names;
        }
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(SAVE_DIR, "*.json")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                names.add(fileName.substring(0, fileName.length() - ".json".length())); // Remove .json extension
            }
        } catch (IOException e) {
            throw new RuntimeException("Elenco salvataggi fallito: " + e.getMessage(), e);
        }
        names.sort(String::compareToIgnoreCase);
        return names;
    }

    public boolean hasAnySave() {
        return !listSaves().isEmpty();
    }

    private Path fileFor(String saveName) {
        String safeName = saveName.replaceAll("[^a-zA-Z0-9 _-]", "_");
        return SAVE_DIR.resolve(safeName + ".json");
    }

}

