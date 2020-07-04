package com.example.myapplication.model;

public class ArtistImage {
    public String name;
    public  String url;
    public String songsArtist;

    public ArtistImage(String name, String url, String songsArtist) {
        this.name = name;
        this.url = url;
        this.songsArtist = songsArtist;
    }

    public ArtistImage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSongsArtist() {
        return songsArtist;
    }

    public void setSongsArtist(String songsArtist) {
        this.songsArtist = songsArtist;
    }
}

