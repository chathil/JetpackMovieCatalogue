package com.app.jetpackmoviecatalogue.data.source.local.entity;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class GenreConverter {
    @TypeConverter
    public ArrayList<Integer> gettingListFromString(String genreIds) {
        ArrayList<Integer> list = new ArrayList<>();

        String[] array = genreIds.split(",");

        for (String s : array) {
            if (!s.isEmpty()) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }

    @TypeConverter
    public String writingStringFromList(ArrayList<Integer> list) {
        String genreIds = "";
        for (int i : list) {
            genreIds += "," + i;
        }
        return genreIds;
    }}
