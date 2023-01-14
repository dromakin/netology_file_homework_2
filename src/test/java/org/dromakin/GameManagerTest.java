package org.dromakin;

import org.junit.jupiter.api.AfterEach;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameManagerTest {

    GameManager gameManager;
    GameProgress gameProgress;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws GameManagerException {
        this.gameManager = new GameManager();
        this.gameManager.uninstalling(); // need to clear after running Main.java
        this.gameManager.installing();
        this.gameProgress = new GameProgress(100, 1, 1, 100.0);
        this.gameManager.setGameProgress(this.gameProgress);
        this.gameManager.saveGame(this.gameManager.getFileSavePathString());
        this.gameManager.zipFiles(this.gameManager.getZipFilePathString(), this.gameManager.getFileSavePathString());
    }

    @org.junit.jupiter.api.Test
    void installing() {
        assertTrue(Files.isDirectory(this.gameManager.getGamePath()));
    }

    @org.junit.jupiter.api.Test
    void saveGame() {
        assertTrue(Files.exists(this.gameManager.getGamePath()));
    }

    @org.junit.jupiter.api.Test
    void zipFiles() {
        assertTrue(Files.exists(this.gameManager.getZipFilePath()));
    }

    @AfterEach
    void tearDown() throws GameManagerException {
        this.gameManager.uninstalling();
    }
}