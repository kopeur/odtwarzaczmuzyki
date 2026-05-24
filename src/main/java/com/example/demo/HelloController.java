package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML private ListView<Song> songview;
    @FXML private Label songlabel;
    @FXML private ImageView albumcover;
    @FXML private ProgressBar songprogress;
    @FXML private Slider volumeslider;
    @FXML private TextArea lyricsArea;

    private MediaPlayer mediaPlayer;
    private ObservableList<Song> masterSongList = FXCollections.observableArrayList();
    private Song currentSong;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                playSelectedSong(newValue);
            }
        });

        songprogress.setOnMouseClicked(event -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double mouseX = event.getX();
                double width = songprogress.getWidth();
                double percent = mouseX / width;
                double totalMillis = mediaPlayer.getTotalDuration().toMillis();
                mediaPlayer.seek(javafx.util.Duration.millis(totalMillis * percent));
            }
        });
    }
    @FXML
    public void handleOpenFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder z muzyką .mp3");

        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            masterSongList.clear();

            File[] files = selectedDirectory.listFiles();
            if (files != null) {
                for (File file : files) {

                    if (file.isFile() && file.getName().endsWith(".mp3")) {
                        try {
                            URL pathUrl = file.toURI().toURL();
                            String baseName = file.getName().replace(".mp3", "");

                            String lyricsText = "Brak tekstu dla tego utworu.";
                            File textFile = new File(selectedDirectory, baseName + ".txt");
                            if (textFile.exists()) {
                                lyricsText = java.nio.file.Files.readString(textFile.toPath());
                            }
                            javafx.scene.image.Image albumImage = null; 
                            File coverFileJpg = new File(selectedDirectory, baseName + ".jpg");
                            File coverFilePng = new File(selectedDirectory, baseName + ".png");

                            if (coverFileJpg.exists()) {
                                albumImage = new javafx.scene.image.Image(coverFileJpg.toURI().toString());
                            } else if (coverFilePng.exists()) {
                                albumImage = new javafx.scene.image.Image(coverFilePng.toURI().toString());
                            }

                            Song newSong = new Song(
                                    pathUrl,
                                    baseName,
                                    "Nieznany wykonawca",
                                    "Brak albumu",
                                    "Inny",
                                    lyricsText,
                                    albumImage 
                            );

                            masterSongList.add(newSong);
                        } catch (Exception e) {
                            System.out.println("Błąd przy wczytywaniu pliku: " + file.getName());
                        }
                    }
                }
            }
            songview.setItems(masterSongList);
        }
    }

    private void playSelectedSong(Song song) {
        currentSong = song;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        Media media = new Media(song.getPath().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.volumeProperty().bind(volumeslider.valueProperty().divide(100));
        mediaPlayer.play();

        songlabel.setText(song.getTitle());

        lyricsArea.setText(song.getLyrics());

        if (song.getCover() != null) {
            albumcover.setImage(song.getCover());
        } else {
            albumcover.setImage(null);
        }

        mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (mediaPlayer.getTotalDuration() != null) {
                double progress = newTime.toMillis() / mediaPlayer.getTotalDuration().toMillis();
                songprogress.setProgress(progress);
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            nextmedia();
        });
    }

    @FXML
    public void playmedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @FXML
    public void pausemedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    public void nextmedia() {
        int currentIndex = songview.getSelectionModel().getSelectedIndex();
        if (currentIndex >= masterSongList.size() - 1) {
            songview.getSelectionModel().select(0);
        } else {
            songview.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    public void prevmedia() {
        int currentIndex = songview.getSelectionModel().getSelectedIndex();
        if (currentIndex <= 0) {
            if (!masterSongList.isEmpty()) {
                songview.getSelectionModel().select(masterSongList.size() - 1);
            }
        } else {
            songview.getSelectionModel().select(currentIndex - 1);
        }
    }

    @FXML
    public void sortAZ() {
        masterSongList.sort((s1, s2) -> s1.getTitle().compareToIgnoreCase(s2.getTitle()));
    }

    @FXML
    public void sortZA() {
        masterSongList.sort((s1, s2) -> s2.getTitle().compareToIgnoreCase(s1.getTitle()));
    }

    @FXML
    public void shuffle() {
        javafx.collections.FXCollections.shuffle(masterSongList);
    }

}
