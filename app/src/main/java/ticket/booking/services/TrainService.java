package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper;
    private static final String TRAINS_PATH = "src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAINS_PATH);
        this.objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        loadTrainListFromFile();
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();

        int sourceIndex = stations.indexOf(source.toLowerCase());
        int destinationIndex = stations.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    private void saveTrainListToFile() {
        File trains = new File(TRAINS_PATH);
        try {
            objectMapper.writeValue(trains, trainList);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadTrainListFromFile() throws IOException {
        File trainsFile = new File(TRAINS_PATH);
        if (!trainsFile.exists() || trainsFile.length() == 0) {
            // First time: file doesn't exist or is empty — create empty train list
            trainList = new ArrayList<>();
            return;
        }
        trainList = objectMapper.readValue(trainsFile, new TypeReference<List<Train>>() {});
    }

    public void addTrain(Train newTrain) {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }


    public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }




}
