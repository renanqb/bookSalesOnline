package com.renan.booksalesonline.domain.enums;

public enum ImageExtension {

    BMP("bmp"),
    GIF("gif"),
    JPG("jpg"),
    JPEG("jpeg"),
    IMG("img"),
    PNG("png"),
    TIFF("tiff");

    private String text = "";

    ImageExtension(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getContentType() {

        return String.format("image/%s", getText());
    }

    public static String getExtensionAcceptedList() {
        return String.format("%s, %s, %s, %s, %s, %s and %s", ImageExtension.values());
    }

    @Override
    public String toString() {
        return getText();
    }
}
