package com.fome.planster.models;

import android.net.Uri;

/**
 * Created by Alex on 29.03.2017.
 */
public class Contact {
    private int id;
    private String name;
    private String photoThumbnailUri;

    public Contact(int id, String name, String photoThumbnailUri) {
        this.id = id;
        this.name = name;
        this.photoThumbnailUri = photoThumbnailUri;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getPhotoThumbnailUri() {
        return Uri.parse(photoThumbnailUri);
    }
}
