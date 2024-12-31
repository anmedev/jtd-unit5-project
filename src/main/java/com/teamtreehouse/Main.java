package com.teamtreehouse;

import com.teamtreehouse.model.Country;
import com.teamtreehouse.model.Util;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // Data table title and headers
        System.out.printf("----------------------------------------------------------------------%n");
        System.out.printf("    World Bank Public Data Set       %n");
        System.out.printf("    Is there a relationship between internet usage and adult literacy?       %n");
        System.out.printf("----------------------------------------------------------------------%n");

        // Executes the program
        run();
    }

    public static void run() {
        String userSelection;
        do {
            System.out.println("\n--- World Bank Public Data Set ---");
            System.out.println("1. Display Country Data");
            System.out.println("2. Display Analysis");
            System.out.println("3. Edit Country");
            System.out.println("4. Add Country");
            System.out.println("5. Delete Country");
            System.out.println("6. Exit");
            System.out.print("\nSelect an option: ");

            userSelection = scanner.nextLine();
            switch (userSelection.toLowerCase()) {
                case "1":
                    displayCountryData();
                    break;
                case "2":
                    displayAnalysis();
                    break;
                case "3":
                    editCountry();
                    break;
                case "4":
                    addCountry();
                    break;
                case "5":
                    deleteCountry();
                    break;
                case "6":
                    System.out.println("Thank you for using our program!");
                    Util.shutdown();
                    return;
                default:
                    System.out.printf("Invalid choice. Try again. %n");
            }
        } while (true); // Ensures the loop continues until "exit" is selected
    }

    // Method to display the country data
    private static void displayCountryData() {
        System.out.printf("%-20s %-40s %-20s %-25s\n", "Code", "Country", "Internet Users", "Literacy");
        System.out.printf("----------------------------------------------------------------------------------------------------%n");
        for (Country country : fetchAllCountries()) {
            BigDecimal internetUsers = country.getInternetUsers();
            BigDecimal adultLiteracyRate = country.getAdultLiteracyRate();

            String internetUsersFormatted = (internetUsers != null)
                    ? internetUsers.setScale(2, RoundingMode.HALF_UP).toString()
                    : "--";
            String adultLiteracyFormatted = (adultLiteracyRate != null)
                    ? adultLiteracyRate.setScale(2, RoundingMode.HALF_UP).toString()
                    : "--";

            System.out.printf("%-20s %-40s %-20s %-25s\n",
                    country.getCode(),
                    country.getName(),
                    internetUsersFormatted,
                    adultLiteracyFormatted);

        }
    }

    // Method to display stats
    private static void displayAnalysis() {
        List<BigDecimal> internetUsersVal = new ArrayList<>();
        List<BigDecimal> adultLiteracyRateVal = new ArrayList<>();

        // Filter null values manually while iterating
        for (Country country : fetchAllCountries()) {
            BigDecimal internetUsers = country.getInternetUsers();
            BigDecimal adultLiteracyRate = country.getAdultLiteracyRate();

            if (internetUsers != null) {
                internetUsersVal.add(internetUsers);
            }
            if (adultLiteracyRate != null) {
                adultLiteracyRateVal.add(adultLiteracyRate);
            }
        }

        // Calculate min and max for internet users
        BigDecimal minInternetUsers = null, maxInternetUsers = null;
        for (BigDecimal value : internetUsersVal) {
            if (minInternetUsers == null || value.compareTo(minInternetUsers) < 0) {
                minInternetUsers = value;
            }
            if (maxInternetUsers == null || value.compareTo(maxInternetUsers) > 0) {
                maxInternetUsers = value;
            }
        }

        // Calculate min and max for adult literacy rate
        BigDecimal minAdultLiteracyRate = null, maxAdultLiteracyRate = null;
        for (BigDecimal value : adultLiteracyRateVal) {
            if (minAdultLiteracyRate == null || value.compareTo(minAdultLiteracyRate) < 0) {
                minAdultLiteracyRate = value;
            }
            if (maxAdultLiteracyRate == null || value.compareTo(maxAdultLiteracyRate) > 0) {
                maxAdultLiteracyRate = value;
            }
        }
        minInternetUsers = minInternetUsers != null ? minInternetUsers.setScale(2, RoundingMode.HALF_UP) : null;
        maxInternetUsers = maxInternetUsers != null ? maxInternetUsers.setScale(2, RoundingMode.HALF_UP) : null;

        minAdultLiteracyRate = minAdultLiteracyRate != null ? minAdultLiteracyRate.setScale(2, RoundingMode.HALF_UP) : null;
        maxAdultLiteracyRate = maxAdultLiteracyRate != null ? maxAdultLiteracyRate.setScale(2, RoundingMode.HALF_UP) : null;

        System.out.println("The minimum amount of Internet Users is " +
                (minInternetUsers != null ? minInternetUsers : "No data") +
                ". The maximum amount of Internet Users is " +
                (maxInternetUsers != null ? maxInternetUsers : "No data") + ".");

        System.out.println("The minimum amount of Adult Literacy Rate is " +
                (minAdultLiteracyRate != null ? minAdultLiteracyRate : "No data") +
                ". The maximum amount of Adult Literacy Rate is " +
                (maxAdultLiteracyRate != null ? maxAdultLiteracyRate : "No data") + ".");

    }

    // Method to find the country by code
    private static Country findCountryByCode(String code) {
        Session session = Util.getSession();
        Country country = session.get(Country.class, code);
        session.close();
        return country;
    }

    // Method to update country data
    private static void editCountry() {
        System.out.print("\nEnter the country code to edit: ");
        String countryCode = scanner.nextLine().toUpperCase();

        // Fetch the existing country
        Country country = findCountryByCode(countryCode);
        if (country == null) {
            System.out.println("No country found with the given code.");
            return;
        }

        System.out.println("Existing country data:");
        System.out.println(country);

        // Prompt user for new values
        System.out.print("Enter new name (leave blank to keep current value): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            country.setName(newName);
        }

        System.out.print("Enter new Internet Users percentage (leave blank to keep current value): ");
        String newInternetUsers = scanner.nextLine();
        if (!newInternetUsers.isEmpty()) {
            try {
                country.setInternetUsers(new BigDecimal(newInternetUsers));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Keeping the current value.");
            }
        }

        System.out.print("Enter new Adult Literacy Rate percentage (leave blank to keep current value): ");
        String newAdultLiteracyRate = scanner.nextLine();
        if (!newAdultLiteracyRate.isEmpty()) {
            try {
                country.setAdultLiteracyRate(new BigDecimal(newAdultLiteracyRate));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Keeping the current value.");
            }
        }

        // Update the database using Hibernate transaction
        Session session = Util.getSession();
        try {
            session.beginTransaction();
            session.merge(country);
            session.getTransaction().commit();
            System.out.println("Country updated successfully.");
            displayCountryData();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Failed to update country: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // Method to add a new country
    private static void addCountry() {
        System.out.println("\n--- Add a New Country ---");
        System.out.print("Enter country code (3 characters): ");
        String code = scanner.nextLine().toUpperCase();

        // Validate code length
        if (code.length() != 3) {
            System.out.println("Invalid country code. It must be exactly 3 characters.");
            return;
        }

        System.out.print("Enter country name: ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            System.out.println("Country name cannot be empty.");
            return;
        }

        System.out.print("Enter Internet Users percentage (leave blank if you don't want to change the data): ");
        String internetUsersInput = scanner.nextLine();
        BigDecimal internetUsers = null;
        if (!internetUsersInput.isEmpty()) {
            try {
                internetUsers = new BigDecimal(internetUsersInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for Internet Users.");
                return;
            }
        }

        System.out.print("Enter Adult Literacy Rate percentage (leave blank if you don't want to change the data): ");
        String literacyRateInput = scanner.nextLine();
        BigDecimal adultLiteracyRate = null;
        if (!literacyRateInput.isEmpty()) {
            try {
                adultLiteracyRate = new BigDecimal(literacyRateInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for Adult Literacy Rate.");
                return;
            }
        }

        // Create a new Country object
        Country newCountry = new Country();
        newCountry.setCode(code);
        newCountry.setName(name);
        newCountry.setInternetUsers(internetUsers);
        newCountry.setAdultLiteracyRate(adultLiteracyRate);

        // Save to the database using Hibernate transaction
        Session session = Util.getSession();
        try {
            session.beginTransaction();
            session.persist(newCountry);
            session.getTransaction().commit();
            System.out.println("Country added successfully.");
            displayCountryData();
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Failed to add country: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // Method to delete a country from the database
    private static void deleteCountry() {
        System.out.print("\nEnter the country code to delete: ");
        String countryCode = scanner.nextLine().toUpperCase();

        // Fetch the country to delete
        Country country = findCountryByCode(countryCode);
        if (country == null) {
            System.out.println("No country found with the given code.");
            return;
        }

        // Confirm deletion with the user
        System.out.println("Country found: " + country);
        System.out.print("Are you sure you want to delete this country? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }

        // Remove the country using Hibernate transaction
        Session session = Util.getSession();
        try {
            session.beginTransaction();
            session.remove(country);
            session.getTransaction().commit();
            System.out.println("Country deleted successfully.");
            displayCountryData(); // Display updated data
        } catch (Exception e) {
            session.getTransaction().rollback();
            System.out.println("Failed to delete country: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    // Method to fetch all countries from the database
    private static List<Country> fetchAllCountries() {
        Session session = Util.getSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);
        criteria.from(Country.class);
        List<Country> countries = session.createQuery(criteria).getResultList();
        session.close();
        return countries;
    }
}