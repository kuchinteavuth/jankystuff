package com.inteavuthkuch.jankystuff.common;

import javax.annotation.Nullable;

public enum ContainerType {
    WOODEN("wooden_crate", 8, 11, UserInterface.CONTAINER_11_BY_8),
    PORTABLE("portable_crate", 8, 11, UserInterface.CONTAINER_11_BY_8),
    BASIC_QUARRY("basic_quarry", 3, 3, UserInterface.BASIC_QUARRY),
    METAL("metal_crate", 9,13, UserInterface.CONTAINER_13_BY_9);

    private final String id;
    private final int row;
    private final int col;
    private final UserInterface gui;

    ContainerType(String id, int row, int col, @Nullable UserInterface gui) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.gui = gui;
    }

    ContainerType(String id, ContainerType copy) {
        this(id, copy.row, copy.col, copy.gui);
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
    public UserInterface getGui() {
        return this.gui;
    }
}
