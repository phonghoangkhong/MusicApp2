package com.example.myapplication.model;

import androidx.annotation.Nullable;

public class Song {
    public String songscategory, songtitle,artist,album_art,songDuration,songLink,mKey;
    int state;
      String artistImage;
    public Song(String songscategory, String songtitle, String artist, String album_art, String songDuration, String songLink,String artistImage) {
      if(songtitle.trim().equals("")){
          songtitle="No title";
      }
        this.songscategory = songscategory;
        this.songtitle = songtitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songLink = songLink;
        this.artistImage=artistImage;


    }

    public String getArtistImage() {
        return artistImage;
    }

    public void setArtistImage(String artistImage) {
        this.artistImage = artistImage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Song() {
    }

    public String getSongscategory() {
        return songscategory;
    }

    public void setSongscategory(String songscategory) {
        this.songscategory = songscategory;
    }

    public String getSongtitle() {
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==this){
            return true;
        }
        if(!(obj instanceof Song)){
            return false;
        }
        Song uploadSong=(Song) obj;
        return this.mKey.equals(uploadSong.mKey);
    }
}
