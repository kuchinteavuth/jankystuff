package com.inteavuthkuch.jankystuff.util;

import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class RunnableItemStackHandler extends ItemStackHandler {

    private final Consumer<Integer> onContentsChanged;

    public RunnableItemStackHandler(int size, Consumer<Integer> onContentsChanged) {
        super(size);
        this.onContentsChanged = onContentsChanged;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.onContentsChanged.accept(slot);
    }
}
