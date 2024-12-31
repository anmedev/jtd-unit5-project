package com.teamtreehouse;

import com.teamtreehouse.model.Country;
import com.teamtreehouse.model.Util;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Data table title and headers
        System.out.printf("----------------------------------------------------------------------%n");
        System.out.printf("    World Bank Public Data Set       %n");
        System.out.printf("    Is there a relationship between internet usage and adult literacy?       %n");
        System.out.printf("----------------------------------------------------------------------%n");
        System.out.printf("%-20s %-40s %-20s %-25s\n", "Code", "Country", "Internet Users", "Literacy");

        displayCountryData();
        displayAnalysis();
    }


    // Method to display the country data
    private static void displayCountryData() {
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

        // Calculate min and max manually
        BigDecimal minInternetUsers = null, maxInternetUsers = null;
        for (BigDecimal value : internetUsersVal) {
            if (minInternetUsers == null || value.compareTo(minInternetUsers) < 0) {
                minInternetUsers = value;
            }
            if (maxInternetUsers == null || value.compareTo(maxInternetUsers) > 0) {
                maxInternetUsers = value;
            }
        }

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

        System.out.println("Internet Users - Min: " + (minInternetUsers != null ? minInternetUsers : "No data") +
                ", Max: " + (maxInternetUsers != null ? maxInternetUsers : "No data"));
        System.out.println("Adult Literacy Rate - Min: " + (minAdultLiteracyRate != null ? minAdultLiteracyRate : "No data") +
                ", Max: " + (maxAdultLiteracyRate != null ? maxAdultLiteracyRate : "No data"));
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