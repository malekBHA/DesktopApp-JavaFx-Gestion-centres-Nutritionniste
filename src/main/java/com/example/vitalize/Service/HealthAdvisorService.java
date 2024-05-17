package com.example.vitalize.Service;

import com.example.vitalize.Entity.FichePatient;

public class HealthAdvisorService {

    public String provideHealthAdvice(FichePatient fichePatient) {
        StringBuilder advice = new StringBuilder();

        // Analyze health data and provide personalized advice
        double bmi = calculateBMI(fichePatient.getWeight(), fichePatient.getHeight());

        // Example: Check BMI and suggest exercises or diet modifications
        if (bmi > 24.9) {
            advice.append("Health Advice:\n- Your BMI is ").append(bmi).append(", which indicates that you are overweight. Consider reducing calorie intake and doing more exercises.\n");
        } else if (bmi < 18.5) {
            advice.append("Health Advice:\n- Your BMI is ").append(bmi).append(", which indicates that you are underweight. Consider increasing calorie intake and doing strength training exercises.\n");
        } else {
            advice.append("Health Advice:\n- Your BMI is ").append(bmi).append(", which is within a normal range. Keep up the good work!\n");
        }

        int waterIntake = calculateWaterIntake(fichePatient.getWeight());
        advice.append("- Drink at least ").append(waterIntake).append(" milliliters of water per day for proper hydration.\n");

        // Example: Check weight and suggest exercises or diet modifications
        if (fichePatient.getWeight() > 80) {
            advice.append("Health Advice:\n- Your weight is above normal. Consider reducing calorie intake and doing more exercises.\n");
        } else {
            advice.append("Health Advice:\n- Your weight is within normal range. Keep up the good work!\n");
        }

        // Analyze muscle mass and suggest strength training
        if (fichePatient.getMuscleMass() < 50) {
            advice.append("- Your muscle mass is low. Consider incorporating strength training exercises into your routine.\n");
        } else {
            advice.append("- Your muscle mass is adequate. Keep doing resistance exercises to maintain it.\n");
        }

        // Analyze height and suggest posture improvement exercises
        if (fichePatient.getHeight() > 160) {
            advice.append("- Your height indicates good posture. However, you can still benefit from posture improvement exercises.\n");
        } else {
            advice.append("- Your height might indicate poor posture. Consider exercises to improve your posture.\n");
        }

        // Analyze allergies and suggest avoiding allergens
        if (fichePatient.getAllergies() != null && !fichePatient.getAllergies().isEmpty()) {
            advice.append("- Be mindful of your allergies and avoid allergens to prevent adverse reactions.\n");
        }

        // Example: Analyze specific allergies
        if (fichePatient.getAllergies() != null && fichePatient.getAllergies().contains("fraise")) {
            advice.append("Health Advice:\n- Avoid strawberries and any foods containing strawberries to prevent allergic reactions.\n");
        }

        // Analyze illnesses and suggest appropriate actions
        if (fichePatient.getIllnesses() != null && !fichePatient.getIllnesses().isEmpty()) {
            advice.append("- Manage your illnesses effectively. Follow your doctor's advice and take necessary precautions.\n");
        }

        // Analyze daily diet and suggest improvements
        String dailyDiet = fichePatient.getBreakfast() + ", " + fichePatient.getMidday() + ", " + fichePatient.getDinner() + ", " + fichePatient.getSnacks();
        if (dailyDiet.contains("fast food") || dailyDiet.contains("sugary snacks")) {
            advice.append("- Your daily diet contains unhealthy items like fast food and sugary snacks. Consider adding more fruits, vegetables, and whole grains.\n");
        }

        // Analyze calorie intake and suggest adjustments
        if (fichePatient.getCalories() > 2000) {
            advice.append("- Your daily calorie intake seems high. Consider reducing it for better health.\n");
        }

        // Add more analysis and advice based on other health data

        return advice.toString();
    }

    private double calculateBMI(double weight, double height) {
        double heightMeters = height / 100; // Convert height from cm to meters
        return Math.round(weight / (heightMeters * heightMeters) * 10.0) / 10.0;
    }

    private int calculateWaterIntake(double weight) {
        // Daily water intake recommendation (30-35 ml per kg of body weight)
        return (int) Math.round(30 * weight);
    }
}
