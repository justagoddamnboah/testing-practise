package edu.course.library;

public class EstimatePolicy {
    public int borrowed(int days) {
        if (days < 1 || days > 14) {
            throw new IllegalArgumentException
            ("you can borrow a book for 1 day at the least and 14 days at the most");
        }
        int price = 50 * days;
        return price;
    }
}