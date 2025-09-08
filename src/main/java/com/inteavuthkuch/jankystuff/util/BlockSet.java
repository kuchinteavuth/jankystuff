package com.inteavuthkuch.jankystuff.util;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

public record BlockSet<T>(Set<T> blocks) {
    @NotNull
    @SafeVarargs
    public static <T> BlockSet<T> of(T... data) {
        return new BlockSet<>(Set.of(data));
    }

    public BlockSet<T> each(@NotNull Consumer<T> consumer) {
        for(T item : blocks) {
            consumer.accept(item);
        }
        // can call each again for another loop
        return this;
    }
}
