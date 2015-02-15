package com.kol.dbPlugin;

public enum DBObjects {

    PROCEDURE("procedures"), VIEW("views"), FUNCTION("functions"), TRIGGER("triggers");

    DBObjects(String dirName) {
        this.dirName = dirName;
    }

    private String dirName;

    public String getDirName() {
        return dirName;
    }
}
