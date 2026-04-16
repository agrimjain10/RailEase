package services;

import java.util.ArrayList;
import java.util.List;
import models.Train;
import utils.FileUtil;

public class TrainService {
    private final String trainFilePath;

    public TrainService(String trainFilePath) {
        this.trainFilePath = trainFilePath;
    }

    public List<Train> getAllTrains() {
        List<String> lines = FileUtil.readLines(trainFilePath);
        List<Train> trains = new ArrayList<>();

        for (String line : lines) {
            trains.add(Train.fromFileString(line));
        }
        return trains;
    }

    public Train findTrainById(String trainId) {
        for (Train train : getAllTrains()) {
            if (train.getTrainId().equalsIgnoreCase(trainId)) {
                return train;
            }
        }
        return null;
    }

    public boolean reserveSeat(String trainId) {
        List<Train> trains = getAllTrains();

        for (Train train : trains) {
            if (train.getTrainId().equalsIgnoreCase(trainId)) {
                boolean booked = train.bookSeat();
                if (booked) {
                    saveTrains(trains);
                }
                return booked;
            }
        }
        return false;
    }

    public List<Train> searchTrains(String source, String destination) {
        List<Train> matches = new ArrayList<>();

        for (Train train : getAllTrains()) {
            boolean sourceMatches = source == null || source.isBlank()
                    || train.getSource().equalsIgnoreCase(source.trim());
            boolean destinationMatches = destination == null || destination.isBlank()
                    || train.getDestination().equalsIgnoreCase(destination.trim());

            if (sourceMatches && destinationMatches) {
                matches.add(train);
            }
        }
        return matches;
    }

    public boolean restoreSeat(String trainId) {
        List<Train> trains = getAllTrains();

        for (Train train : trains) {
            if (train.getTrainId().equalsIgnoreCase(trainId)) {
                boolean cancelled = train.cancelSeat();
                if (cancelled) {
                    saveTrains(trains);
                }
                return cancelled;
            }
        }
        return false;
    }

    private void saveTrains(List<Train> trains) {
        List<String> lines = new ArrayList<>();

        for (Train train : trains) {
            lines.add(train.toFileString());
        }
        FileUtil.writeLines(trainFilePath, lines);
    }
}
