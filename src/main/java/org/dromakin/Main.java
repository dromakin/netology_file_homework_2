package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {
            // Create game folder
            GameManager gameManager = new GameManager();
            gameManager.installing();

            // start saving your progress in game
            GameProgress save = new GameProgress(100, 1, 1, 100.0);
            gameManager.setGameProgress(save);
            gameManager.saveGame();
            String saveFile1 = gameManager.getFileSavePath();

            save = new GameProgress(80, 3, 4, 500.0);
            gameManager.setGameProgress(save);
            gameManager.saveGame();
            String saveFile2 = gameManager.getFileSavePath();

            save = new GameProgress(50, 10, 10, 1000.0);
            gameManager.setGameProgress(save);
            gameManager.saveGame();
            String saveFile3 = gameManager.getFileSavePath();

            // save all progress to files
            gameManager.saveGame(saveFile1, saveFile2, saveFile3);

            // zip all files in folder
            String zipFilePath = gameManager.getZipFilePath();
            gameManager.zipFiles(zipFilePath, saveFile1, saveFile2, saveFile3);

        } catch (GameManagerException e) {
            logger.error(e.getMessage(), e);
        }


    }
}