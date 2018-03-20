package com.example.kayda.mendle;

import android.support.annotation.NonNull;

/**
 * Created by Kayda on 3/18/2018.
 */

public class UserId {
    public String userId;
    public <T extends UserId> T withId(@NonNull final String id){
        this.userId=id;
        return (T) this;
    }
}
