package io.mikaple.endertech.utils;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class EnderBookContextTransformer {

    public static List<Component> fromList(List<String> strings) {
        List<Component> components = new ArrayList<>();
        for (String string : strings) {
            components.add(fromString(string));
        }
        return components;
    }

    public static Component fromString(String string) {
        Component component = Component.literal(string);
        return component;
    }
}
