package com.teamtreehouse.model;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Country {
    @Id
    @Column(length = 3)
    private String code;
    @Column(length = 32)
    private String name;
    @Column(precision = 11, scale = 8)
    private BigDecimal internetUsers;
    @Column(precision = 11, scale = 8)
    private BigDecimal adultLiteracyRate;

    // Default constructor for JPA
    public Country(){}

    @Override
    public String toString() {
        return "Country{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", internetUsers='" + internetUsers + '\'' +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(BigDecimal internetUsers) {
        this.internetUsers = internetUsers;
    }

    public BigDecimal getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(BigDecimal adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }
}