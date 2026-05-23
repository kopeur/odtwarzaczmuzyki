package com.example.demo;
import javafx.scene.image.Image;
import java.net.URL;

public class Song {
    private URL path;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String lyrics;
    private Image cover;

    public Song(URL path, String title, String artist, String album, String genre, String lyrics, Image cover) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.lyrics = lyrics;
        this.cover = cover;
    }

    public URL getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public String getLyrics() {
        return lyrics;
    }

    public Image getCover() {
        return cover;
    }

    public void setPath(URL path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }
    @Override
    public String toString() {
        if (artist != null && !artist.equals("Nieznany wykonawca")) {
            return artist + " - " + title;
        }
        return title;
    }
}


