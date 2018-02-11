package org.app.zeroresearchtravel.androidhackdataapp;

public class CustomerInfo {
    private int age;
    private int leaves;
    private int gender_entry;
    private int budget_entry;

    public CustomerInfo(int age, int leaves, int selectedGenderItem, int selectedBudgetItem) {
        this.age = age;
        this.leaves = leaves;
        gender_entry = selectedGenderItem;
        budget_entry = selectedBudgetItem;
    }

    public int getAge() {
        return age;
    }

    public int getBudget_entry() {
        return budget_entry;
    }

    public int getGender_entry() {
        return gender_entry;
    }

    public int getLeaves() {
        return leaves;
    }
}
