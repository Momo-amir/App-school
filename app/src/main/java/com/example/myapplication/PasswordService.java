package com.example.myapplication;

public class PasswordService {
    private final String name;
    private final String url;
    private final int iconResId;
    private final int backgroundColor;
    private final String id;

    public PasswordService(String name, String url, int iconResId, int backgroundColor, String id) {
        this.name = name;
        this.url = url;
        this.iconResId = iconResId;
        this.backgroundColor = backgroundColor;
        this.id = id;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public int getIconResId() { return iconResId; }
    public int getBackgroundColor() { return backgroundColor; }
    public String getId() { return id; }
}