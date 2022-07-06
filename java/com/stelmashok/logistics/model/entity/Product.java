package com.stelmashok.logistics.model.entity;

import java.util.Objects;

public class Product extends AbstractEntity {
    private String title;
    private int weight;
    private String description;

    public String getTitle() {
        return title;
    }

    public int getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return weight == product.weight && title.equals(product.title) && description.equals(product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, weight, description);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("title='").append(title).append('\'');
        sb.append(", weight=").append(weight);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
