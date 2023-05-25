package com.esdev.sikadis.temp;

import com.esdev.sikadis.models.Artikel;

import java.util.ArrayList;
import java.util.List;

public class TemporaryData {
    private static List<Artikel> artikelList = new ArrayList<>();

    public static List<Artikel> getArtikelList() {
        return artikelList;
    }

    public static void setArtikelList(List<Artikel> list) {
        artikelList = list;
    }
}