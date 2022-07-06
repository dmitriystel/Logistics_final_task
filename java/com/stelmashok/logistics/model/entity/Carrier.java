package com.stelmashok.logistics.model.entity;

import java.util.Objects;

public class Carrier extends AbstractEntity{
    private String carrierName;
    private String truckNumber;

    public String getCarrierName() {
        return carrierName;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Carrier carrier = (Carrier) o;
        return Objects.equals(carrierName, carrier.carrierName) && Objects.equals(truckNumber, carrier.truckNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), carrierName, truckNumber);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Carrier{");
        sb.append("carrierName='").append(carrierName).append('\'');
        sb.append(", truckNumber='").append(truckNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
