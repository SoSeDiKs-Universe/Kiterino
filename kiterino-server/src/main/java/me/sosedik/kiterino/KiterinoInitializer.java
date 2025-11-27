package me.sosedik.kiterino;

import me.sosedik.kiterino.modifier.item.ItemModifiersHandlerImpl;

public class KiterinoInitializer {

    public static void initPostConfig() {
        ItemModifiersHandlerImpl.init();
    }

}
