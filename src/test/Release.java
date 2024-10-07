package test;

class Release {
    int deliveryDay;
    int validationDays;

    public Release(int deliveryDay, int validationDays) {
        this.deliveryDay = deliveryDay;
        this.validationDays = validationDays;
    }

    public int getEndDay() {
        return deliveryDay + validationDays - 1; // Calculates the end day
    }

    public String getDeliveryDay() {
        return Integer.toString(deliveryDay);
    }
}

