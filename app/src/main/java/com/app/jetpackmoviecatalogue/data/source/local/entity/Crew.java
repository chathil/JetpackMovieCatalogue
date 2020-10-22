package com.app.jetpackmoviecatalogue.data.source.local.entity;

public class Crew {

    private int id;

    private String name;

    private String character;

    private String photo;

    public Crew(int id, String name, String character, String photo) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getPhoto() {
        return photo;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
