package com.app.jetpackmoviecatalogue.data.source.remote.response;

public class CrewResponse {
    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getPhoto() {
        return photo;
    }

    public CrewResponse(int id, String name, String character, String photo) {
        this.name = name;
        this.character = character;
        this.photo = photo;
        this.id = id;
    }

    private String name, character, photo;

    public int getId() {
        return id;
    }

    private int id;
}
