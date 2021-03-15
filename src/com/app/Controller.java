package com.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
	@FXML private Label label;
	@FXML private ComboBox<String> trackSpeeds;
	@FXML private Slider volumeSlider;
	@FXML private ProgressBar progressBar;

	private Media media;
	private MediaPlayer player;

	private List<File> songs = new ArrayList<>();
	private File currSong;
	private double currSpeed = 1;
	private double currVolume = 0.5;

	private String[] speeds = {"25%", "50%", "75%", "100%", "125%", "150%", "175%"};
	private ObservableList<String> speedList = FXCollections.observableList(Arrays.asList(speeds));

	private boolean isPlaying = false;
	private Timer timer;
	private TimerTask task;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		songs.add(new File("Avicii  Wake Me Up Lyric Video.mp3"));
		songs.add(new File("Calvin Harris Ft Ellie Goulding  Outside Liu Remix.mp3"));
		songs.add(new File("TOULIVER X SLIMV  EM ƠI CỨ VUI  OFFICIAL VIDEO.mp3"));

		trackSpeeds.setItems(speedList);

		setCurrSong(songs.get(0));

		volumeSlider.valueProperty().addListener((observableValue, oldVal, newVal) -> {
			currVolume = (Double) newVal;
			player.setVolume(currVolume);
		});
	}

	public void playSong(ActionEvent event) {
		player.play();
		isPlaying = true;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				while(isPlaying) {
					double currSec = player.getCurrentTime().toSeconds();
					double totalSec = media.getDuration().toSeconds();
					progressBar.setProgress(currSec/totalSec);
				}
			}
		};
		timer.schedule(task, 0);
	}

	public void pauseSong(ActionEvent event) {
		player.pause();
		isPlaying = false;
		timer.cancel();
		timer.purge();
	}

	public void nextSong(ActionEvent event) {
		player.pause();
		setCurrSong(songs.get((songs.indexOf(currSong) + 1 >= songs.size()) ? 0 : songs.indexOf(currSong) + 1));
		playSong(null);
	}

	public void prevSong(ActionEvent event) {
		player.pause();
		setCurrSong(songs.get((songs.indexOf(currSong) - 1 < 0) ? songs.size() - 1 : songs.indexOf(currSong) - 1));
		playSong(null);
	}

	public void resetSong(ActionEvent event) {
		player.seek(Duration.seconds(0));
		progressBar.setProgress(0);
	}

	public void changeSpeed(ActionEvent event) {
		String speedStr = trackSpeeds.getSelectionModel().getSelectedItem();
		double speed = Double.parseDouble(speedStr.substring(0, speedStr.indexOf("%")));
		player.setRate(speed * 0.01);
		currSpeed = player.getRate();
	}

	public void increaseVolumeByScrolling() {
		volumeSlider.setValue(volumeSlider.getValue() + 0.1);
	}

	public void decreaseVolumeByScrolling() {
		volumeSlider.setValue(volumeSlider.getValue() - 0.1);
	}

	public void setCurrSong(File song) {
		currSong = song;
		media = new Media(currSong.toURI().toString());
		player = new MediaPlayer(media);
		player.setRate(currSpeed);
		player.setVolume(currVolume);
		label.setText(song.getName());
	}
}
