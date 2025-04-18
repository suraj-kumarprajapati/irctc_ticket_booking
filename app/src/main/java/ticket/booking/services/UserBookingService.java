package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";
    private ObjectMapper objectMapper;




    public UserBookingService(User user) throws IOException {
        this.user = user;
        this.objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        this.objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        loadUserListFromFile();
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(u -> {
            return u.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }
        catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException{
        File users = new File(USERS_PATH);
        objectMapper.writeValue(users, userList);
    }

    private void loadUserListFromFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        if (!usersFile.exists() || usersFile.length() == 0) {
            // First time: file doesn't exist or is empty — create empty user list
            userList = new ArrayList<>();
            return;
        }
        userList = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {});
    }

    public boolean isUserLoggedIn() {
        Optional<User> fetchedUser = userList.stream().filter(user1 -> {
            return user.getName().equals(user1.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();

        return fetchedUser.isPresent();
    }

    public void fetchBookings() {
        if(isUserLoggedIn()) {
            user.printTickets();
        }
        else {
            System.out.println("Login first to fetch booking information.....");
        }
    }

    public Boolean cancelBooking(String ticketId) {
        // step 1 : check if user is logged in or not
        // step 2 :

        return Boolean.TRUE;
    }

    public List<Train> getTrains(String source, String destination) {
        return new ArrayList<Train>();
    }

    public List<List<Integer>> fetchSeats(Train train, int row, int seat) {
        return new ArrayList<List<Integer>>();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        return Boolean.TRUE;
    }
}
