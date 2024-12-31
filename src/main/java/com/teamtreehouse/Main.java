package com.teamtreehouse;

import com.teamtreehouse.model.Country;
import com.teamtreehouse.model.Util;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.printf("----------------------------------------------------------------------%n");
        System.out.printf("    World Bank Public Data Set       %n");
        System.out.printf("    Is there a relationship between internet usage and adult literacy?       %n");
        System.out.printf("----------------------------------------------------------------------%n");
        System.out.printf("%-20s %-40s %-20s %-25s\n", "Code", "Country", "Internet Users", "Literacy");

        // Displays the country data
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