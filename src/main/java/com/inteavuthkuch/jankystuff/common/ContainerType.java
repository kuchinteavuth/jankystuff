package com.inteavuthkuch.jankystuff.common;

import javax.annotation.Nullable;

public enum ContainerType {
    WOODEN("wooden_crate", 8, 11, 0,UserInterface.CONTAINER_11_BY_8),
    PORTABLE("portable_crate", 8, 11, 0,UserInterface.CONTAINER_11_BY_8),
    BASIC_QUARRY("basic_quarry", 3, 3, 0,UserInterface.BASIC_QUARRY),
    METAL("metal_crate", 9,13, 0,UserInterface.CONTAINER_13_BY_9),
    BLOCK_BREAKER("block_breaker", 3, 6, 4, UserInterface.BLOCK_BREAKER)
    ;

    private final String id;
    private final int row;
    private final int col;
    private final int additionalSlot;
    private final UserInterface gui;

    ContainerType(String id, int row, int col, int additionalSlot, @Nullable UserInterface gui) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.gui = gui;
        this.additionalSlot = additionalSlot;
    }

    public String getId() {
        return this.id;
    }
    public int getSize() {
        return (this.row * this.col) + this.additionalSlot;
    }
    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }

    public int getAdditionalSlot() {
        return this.additionalSlot;
    }
    public boolean hasAdditionalSlot() {
        return this.additionalSlot > 0;
    }

    public UserInterface getGui() {
        return this.gui;
    }
}
