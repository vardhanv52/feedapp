package com.vishnu.feedapplication.models;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

import java.io.Serializable;
import java.util.Date;

public class _Post implements Serializable
{
    private String name;
    private String message;
    private String createdAT, updatedAt;
    private String id;
    private String post_id;

    public _Post(Context context, String message, boolean isUpdating) {
        if(isUpdating) {
            this.message = message;
            this.updatedAt = Constants.format.format(new Date());
        } else {
            this.message = message;
            this.name = Helper.getUserData(context, Constants.keyDisplayName);
            this.createdAT = Constants.format.format(new Date());
            this.updatedAt = this.createdAT;
            this.id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    public String getCreatedAT() {
        return createdAT;
    }

    public void setCreatedAT(String createdAT) {
        this.createdAT = createdAT;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
