package com.dam2d.pedrojg2ot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChampionDataHolder {
    private ArrayList<String> names;
    private ArrayList<String> ids;
    public Map<String, Champion> champions = new HashMap<String, Champion>();

    public ArrayList<String> getNames() {
        return this.names;
    }

    public ArrayList<String> getIds() {
        return this.ids;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public void addChampion(String name, Champion champion) throws NoSuchFieldException {
        //Class champ = champion.getClass();
        this.champions.put(name, champion);
        System.out.println(this.champions.get(name));
    }

    public Champion getChampion(String name) {
        return this.champions.get(name);
    }

    public Champion getChampionById(String championId) {
        for (Map.Entry<String, Champion> entry : this.champions.entrySet()) {
            if (entry.getValue().getKey().equals(championId)) {
                return this.champions.get(entry.getKey());
            }
        }
        return new Champion("", "", "", "", "", "", "", "");
    }

    private static final ChampionDataHolder holder = new ChampionDataHolder();
    public static ChampionDataHolder getInstance() {
        return holder;
    }
}
