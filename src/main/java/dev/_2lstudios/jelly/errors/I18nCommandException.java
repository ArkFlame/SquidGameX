package dev._2lstudios.jelly.errors;

public class I18nCommandException extends Exception {
    private String key;

    public I18nCommandException(String key, String message) {
        super(message);
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
