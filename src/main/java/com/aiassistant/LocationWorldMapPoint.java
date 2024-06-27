package com.aiassistant;

import net.runelite.api.ItemID;
import net.runelite.api.Point;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;
import net.runelite.client.util.AsyncBufferedImage;

import java.awt.image.BufferedImage;

public class LocationWorldMapPoint extends WorldMapPoint {

    LocationWorldMapPoint(Location location, ItemManager itemManager) {
        super(location.worldPoint, null);

        AsyncBufferedImage img = itemManager.getImage(location.itemID);
        BufferedImage bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        bimg.getGraphics().drawImage(img, 0, 0, null);

        this.setJumpOnClick(true);
        this.setSnapToEdge(true);
        this.setImage(bimg);
        this.setName(location.name());
        this.setImagePoint(new Point(img.getWidth() / 2, img.getHeight()));
    }
}
