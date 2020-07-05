package com.example.myapplication.model;

import java.util.List;

public class LoveSongUser {
    String userName;
    List<Song> listSong;
    public LoveSongUser() {
    }

    public LoveSongUser(String userName, List<Song> listSong) {
        this.userName = userName;
        this.listSong = listSong;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }
}
