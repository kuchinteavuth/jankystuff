package com.inteavuthkuch.jankystuff.common;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class JankyCache {

    public static class Items {
        private final static HashMap<String, List<ItemStack>> caches = new HashMap<>();

        public static List<ItemStack> tryGet(String id) {
            return caches.computeIfAbsent(id, nId -> new ArrayList<>());
        }

        public static void set(String id, List<ItemStack> stacks) {
            var data = caches.get(id);
            if(data == null){
                caches.computeIfAbsent(id, nId -> stacks);
            }else{
                caches.put(id, stacks);
            }
        }
    }
}
