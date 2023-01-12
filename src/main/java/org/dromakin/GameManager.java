/*
 * File:     GameManager
 * Package:  org.dromakin
 * Project:  netology_file_homework_2
 *
 * Created by dromakin as 11.01.2023
 *
 * author - dromakin
 * maintainer - dromakin
 * version - 2023.01.11
 */

package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.dromakin.FileConstants.*;
import static org.dromakin.FileConstants.ICONS_DIR;

public class GameManager {

    private static final Logger logger = LogManager.getLogger(GameManager.class);

    private static final String CREATED_DIR = "Created all folders: {}";
    private static final String CREATED_FILE = "Created file: {}";

    private GameProgress gameProgress;

    private Path saveGameDir;

    private int saveCount;

    public GameManager() {
        // no instances
        this.saveCount = 0;
    }

    public void setGameProgress(GameProgress gameProgress) {
        this.gameProgress = gameProgress;
        this.saveCount++;
    }

    public String getFileSavePath() {
        return Paths.get(this.saveGameDir.toString(), String.format(SAVE_FILE_DAT, saveCount)).toString();
    }

    public String getZipFilePath() {
        return Paths.get(this.saveGameDir.toString(), ZIP_FILE).toString();
    }

    public void installing() throws GameManagerException {
        logger.info("Start installing Game...");

        Path rootPath = Paths.get(TMP_DIR).toAbsolutePath();
        logger.info("Find root path: {}", rootPath);

        Path gamePath = Paths.get(rootPath.toString(), GAME_DIR);
        logger.info("Get Games path: {}", gamePath);

        // Games/
        Path srcDir = Paths.get(gamePath.toString(), SRC_DIR);
        logger.info("Get src dir path: {}", srcDir);

        Path resDir = Paths.get(gamePath.toString(), RES_DIR);
        logger.info("Get res dir path: {}", resDir);

        this.saveGameDir = Paths.get(gamePath.toString(), SAVE_GAME_DIR);
        logger.info("Get savegames dir path: {}", this.saveGameDir);

        Path fullDir;
        try {
            fullDir = Files.createDirectories(saveGameDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create savegames dir with path: " + saveGameDir);
        }
        logger.info(CREATED_DIR, saveGameDir);

        // Games/src
        Path mainDir = Paths.get(srcDir.toString(), MAIN_DIR);
        logger.info("Get main dir path: {}", mainDir);

        try {
            fullDir = Files.createDirectories(mainDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create main dir with path: " + mainDir);
        }
        logger.info(CREATED_DIR, fullDir);

        Path testDir = Paths.get(srcDir.toString(), TEST_DIR);
        logger.info("Get test dir path: {}", testDir);

        try {
            fullDir = Files.createDirectories(testDir);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create test dir with path: " + testDir);
        }
        logger.info(CREATED_DIR, fullDir);

        // Games/src/main
        Path mainFilePath = Paths.get(mainDir.toString(), MAIN_FILE);
        logger.info("Get main file path: {}", mainFilePath);

        try {
            fullDir = Files.createFile(mainFilePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create Main.java with path: " + mainFilePath);
        }
        logger.info(CREATED_FILE, fullDir);

        Path utilFilePath = Paths.get(mainDir.toString(), UTIL_FILE);
        logger.info("Get util file path: {}", utilFilePath);

        try {
            fullDir = Files.createFile(utilFilePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create Utils.java with path: " + utilFilePath);
        }
        logger.info(CREATED_FILE, fullDir);

        // Games/res/
        Path drawablePath = Paths.get(resDir.toString(), DRAWABLES_DIR);
        logger.info("Get drawables dir path: {}", drawablePath);

        try {
            fullDir = Files.createDirectories(drawablePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create drawable dir with path: " + drawablePath);
        }
        logger.info(CREATED_DIR, fullDir);

        Path vectorPath = Paths.get(resDir.toString(), VECTORS_DIR);
        logger.info("Get vector dir path: {}", vectorPath);

        try {
            fullDir = Files.createDirectories(vectorPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create vector dir with path: " + vectorPath);
        }
        logger.info(CREATED_DIR, fullDir);

        Path iconsPath = Paths.get(resDir.toString(), ICONS_DIR);
        logger.info("Get icons dir path: {}", iconsPath);

        try {
            fullDir = Files.createDirectories(iconsPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new GameManagerException("Can't create vector dir with path: " + iconsPath);
        }
        logger.info(CREATED_DIR, fullDir);

        logger.info("Game installed!");
    }

    public void saveGame(String... paths) throws GameManagerException {
        logger.info("Saving game process start...");
        for (String path : paths) {
            try (
                    FileOutputStream fout = new FileOutputStream(path);
                    ObjectOutputStream out = new ObjectOutputStream(fout)
            ) {
                out.writeObject(this.gameProgress);
                out.flush();
            } catch (IOException e) {
                throw new GameManagerException("Can't Serialize object!", e);
            }
        }
        logger.info("Game progress saved!");
    }

    public void zipFiles(String zipFilePath, String... paths) throws GameManagerException {
        logger.info("Game progress compressing...");
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath);
                ZipOutputStream zipOut = new ZipOutputStream(fos)
        ) {
            for (String path : paths) {

                File fileToZip = new File(path);

                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }

                boolean result = Files.deleteIfExists(Paths.get(path));
                if (!result) {
                    throw new GameManagerException("Can't delete file!");
                }
            }

        } catch (FileNotFoundException e) {
            throw new GameManagerException("File not found!", e);
        } catch (IOException e) {
            throw new GameManagerException(e.getMessage(), e);
        }
        logger.info("Game progress compressed!");
    }

}
