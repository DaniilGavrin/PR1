package ru.bytewizard.pr1;

public class Story {
    private int id;
    private String title;
    private String imageUrl;
    private String author;

    // Конструкторы, геттеры и сеттеры
    public Story(int id, String title, String imageUrl, String author) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAuthor() {
        return author;
    }
}
