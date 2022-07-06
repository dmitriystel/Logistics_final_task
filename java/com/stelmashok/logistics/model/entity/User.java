package com.stelmashok.logistics.model.entity;

import java.util.Objects;

public class User extends AbstractEntity {
    private String customerName;
    private String login;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private UserStatus status;
    private UserRole role;

    public String getCustomerName() {
        return customerName;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserRole getRole() {
        return role;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return customerName.equals(user.customerName) && login.equals(user.login) && email.equals(user.email) && name.equals(user.name) && surname.equals(user.surname) && phone.equals(user.phone) && status == user.status && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerName, login, email, name, surname, phone, status, role);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("customerName='").append(customerName).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", status=").append(status);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}
