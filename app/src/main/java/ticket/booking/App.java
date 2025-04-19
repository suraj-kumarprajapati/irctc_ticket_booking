package ticket.booking;

import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.utils.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        System.out.println("IRCTC app running................");

        Scanner scanner = new Scanner(System.in);
        UserBookingService userBookingService;
        int option = 0;

        try{
            // this will load all the users from the localDb to userBookingService.userList
            userBookingService = new UserBookingService();
        }catch(IOException ex){
            ex.printStackTrace();
            System.out.println("There is something wrong");
            return;
        }

        User currentUser = null;

        while(option != 7) {
            System.out.println();
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            System.out.println();
            option = scanner.nextInt();

            switch (option) {

                case 1 :
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignup = new User(UUID.randomUUID().toString(), nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>());
                    boolean isSignUpSuccessful = userBookingService.signUp(userToSignup);
                    if(!isSignUpSuccessful) {
                        System.out.println("Incorrect credentials....Signup failed.....");
                        break;
                    }

                    System.out.println("Signup successful....");
                    break;

                case 2 :
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(UUID.randomUUID().toString(), nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>());
                    try{
                        userBookingService = new UserBookingService(userToLogin);
                        boolean isLoginSuccessful = userBookingService.loginUser();
                        if(!isLoginSuccessful) {
                            System.out.println("Incorrect credentials....Login failed.....");
                            break;
                        }
                        System.out.println("Login successful.....");
                    }catch (IOException ex){
                        return;
                    }
                    break;

                case 3 :

                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBookings();
                    break;

                case 4 :

                case 5 :

                case 6 :
                    // step 2 : get the ticket id from the user
                    String tId = null;
                    System.out.println("Enter the ticket id to be canceled : ");
                    tId = scanner.next();
                    Boolean canceled = userBookingService.cancelBooking(tId);
                    if(canceled) {
                        System.out.println("Ticket with ID " + tId + " has been canceled.");
                    }
                    else {
                        System.out.println("Booking cancellation failed....");
                    }
                    break;

                case 7 :


                default:
                    break;
            }
        }
    }
}
