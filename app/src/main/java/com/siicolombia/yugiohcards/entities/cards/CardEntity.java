package com.siicolombia.yugiohcards.entities.cards;

public class CardEntity {
    long id;
    String name;
    String type;
    String frameType;
    String desc;
    String race;
    String archetype;
    String ygoprodeck_url;
    CardSet[] card_sets;
    CardImage[] card_images;
    CardPrice[] card_prices;
    Long atk;
    Long def;
    Long level;
    String attribute;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFrameType() {
        return frameType;
    }

    public String getDesc() {
        return desc;
    }

    public String getRace() {
        return race;
    }

    public String getArchetype() {
        return archetype;
    }

    public String getYgoprodeckUrl() {
        return ygoprodeck_url;
    }

    public CardSet[] getCardSets() {
        return card_sets;
    }

    public CardImage[] getCardImages() {
        return card_images;
    }

    public CardPrice[] getCardPrices() {
        return card_prices;
    }

    public Long getAtk() {
        return atk;
    }

    public Long getDef() {
        return def;
    }

    public Long getLevel() {
        return level;
    }

    public String getAttribute() {
        return attribute;
    }
}

