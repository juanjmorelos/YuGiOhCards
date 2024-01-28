package com.siicolombia.yugiohcards.entities.cards;

public class CardImage {
    long id;
    String image_url;
    String image_url_mall;
    String image_url_cropped;

    public long getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getImage_url_mall() {
        return image_url_mall;
    }

    public String getImage_url_cropped() {
        return image_url_cropped;
    }
}
