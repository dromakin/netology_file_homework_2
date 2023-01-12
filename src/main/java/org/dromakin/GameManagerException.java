/*
 * File:     GameManagerException
 * Package:  org.dromakin
 * Project:  netology_file_homework_2
 *
 * Created by dromakin as 12.01.2023
 *
 * author - dromakin
 * maintainer - dromakin
 * version - 2023.01.12
 */

package org.dromakin;

public class GameManagerException extends Exception {

    public GameManagerException(String s) {
        super(s);
    }

    public GameManagerException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
