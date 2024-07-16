package com.inteavuthkuch.jankystuff.common;

import javax.annotation.Nullable;

public enum ContainerType {
    WOODEN("wooden_crate", 8, 11, 1,UserInterface.CONTAINER_11_BY_8),
    PORTABLE("portable_crate", 8, 11, 1,UserInterface.CONTAINER_11_BY_8),
    BASIC_QUARRY("basic_quarry", 3, 3, 1,UserInterface.BASIC_QUARRY),
    METAL("metal_crate", 9,13, 2,UserInterface.CONTAINER_13_BY_9);

    private final String id;
    private final int row;
    private final int col;
    private final int totalPage;
    private final UserInterface gui;

    ContainerType(String id, int row, int col, int totalPage, @Nullable UserInterface gui) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.gui = gui;
        this.totalPage = totalPage;
    }

    public String getId() {
        return this.id;
    }
    public int getSize() {
        return this.row * this.col;
    }
    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }
    public int getTotalPage() {
        return this.totalPage;
    }
    public UserInterface getGui() {
        return this.gui;
    }
}
