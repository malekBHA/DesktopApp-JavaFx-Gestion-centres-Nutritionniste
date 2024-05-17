package com.example.vitalize.Entity;

public class FichePatient {
    private int id;
    private int userId; // User ID
    private int weight;
    private int muscleMass;
    private int height;
    private String allergies;
    private String illnesses;
    private String breakfast;
    private String midday;
    private String dinner;
    private String snacks;
    private int calories;
    private String other;

    // Constructors
    public FichePatient() {
        // Default constructor
    }

    public FichePatient(int id, int userId, int weight, int muscleMass, int height, String allergies, String illnesses,
                        String breakfast, String midday, String dinner, String snacks, int calories, String other) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.height = height;
        this.allergies = allergies;
        this.illnesses = illnesses;
        this.breakfast = breakfast;
        this.midday = midday;
        this.dinner = dinner;
        this.snacks = snacks;
        this.calories = calories;
        this.other = other;
    }

    public FichePatient(int userId, int weight, int muscleMass, int height, String allergies, String illnesses, String breakfast, String midday, String dinner, String snacks, int calories, String other) {
        this.userId = userId;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.height = height;
        this.allergies = allergies;
        this.illnesses = illnesses;
        this.breakfast = breakfast;
        this.midday = midday;
        this.dinner = dinner;
        this.snacks = snacks;
        this.calories = calories;
        this.other = other;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(int muscleMass) {
        this.muscleMass = muscleMass;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getIllnesses() {
        return illnesses;
    }

    public void setIllnesses(String illnesses) {
        this.illnesses = illnesses;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getMidday() {
        return midday;
    }

    public void setMidday(String midday) {
        this.midday = midday;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getSnacks() {
        return snacks;
    }

    public void setSnacks(String snacks) {
        this.snacks = snacks;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "FichePatient{" +
                "id=" + id +
                ", userId=" + userId +
                ", weight=" + weight +
                ", muscleMass=" + muscleMass +
                ", height=" + height +
                ", allergies='" + allergies + '\'' +
                ", illnesses='" + illnesses + '\'' +
                ", breakfast='" + breakfast + '\'' +
                ", midday='" + midday + '\'' +
                ", dinner='" + dinner + '\'' +
                ", snacks='" + snacks + '\'' +
                ", calories=" + calories +
                ", other='" + other + '\'' +
                '}';
    }
}
