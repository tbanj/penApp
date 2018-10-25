package com.practicaleducationnetwork.penapp.models;

public class Tutor {
    private String firstName;
    private String lastName;
    private String nameOfSchool;
    private String districtOfSchool;
    private String cityOfSchool;
    private String profileImage;
    private boolean isVerified;
    private String phoneNumber;
    private String password;

    public Tutor() {
    }

    public Tutor(String firstName, String lastName, String nameOfSchool, String districtOfSchool, String cityOfSchool, String profileImage, boolean isVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nameOfSchool = nameOfSchool;
        this.districtOfSchool = districtOfSchool;
        this.cityOfSchool = cityOfSchool;
        this.profileImage = profileImage;
        this.isVerified = isVerified;
    }

    public Tutor(String firstName, String lastName, String nameOfSchool, String districtOfSchool, String cityOfSchool, boolean isVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nameOfSchool = nameOfSchool;
        this.districtOfSchool = districtOfSchool;
        this.cityOfSchool = cityOfSchool;
        this.isVerified = isVerified;
    }

    public String getPseudoEmail(){
        return "234" + getPhoneNumber()+"@pen.com";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameOfSchool() {
        return nameOfSchool;
    }

    public void setNameOfSchool(String nameOfSchool) {
        this.nameOfSchool = nameOfSchool;
    }

    public String getDistrictOfSchool() {
        return districtOfSchool;
    }

    public void setDistrictOfSchool(String districtOfSchool) {
        this.districtOfSchool = districtOfSchool;
    }

    public String getCityOfSchool() {
        return cityOfSchool;
    }

    public void setCityOfSchool(String cityOfSchool) {
        this.cityOfSchool = cityOfSchool;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nameOfSchool='" + nameOfSchool + '\'' +
                ", districtOfSchool='" + districtOfSchool + '\'' +
                ", cityOfSchool='" + cityOfSchool + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}
