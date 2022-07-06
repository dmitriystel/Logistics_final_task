package com.stelmashok.logistics.model.entity;

import java.util.Objects;

public class Unloading extends AbstractEntity{

    private String country;
    private String city;

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Unloading unloading = (Unloading) o;
        return Objects.equals(country, unloading.country) && Objects.equals(city, unloading.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), country, city);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Unloading{");
        sb.append("country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
