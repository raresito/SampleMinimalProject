package com.example.cristianbaita.sampleminimalproject.pojo.views;

import com.example.cristianbaita.sampleminimalproject.pojo.interfaces.SimpleObservable;

/**
 * Created by cristian.baita on 3/16/2016.
 */
public class MainModel extends SimpleObservable<MainModel>{
    private String username;
    private String password;

    public void notifyChanged(){
        notifyObservers();
    }

    public MainModel()
    {
        setUsername("");
        setPassword("");
    }

    public String getUsername()
    {
        return username;
    }

    public final void setUsername(String username, boolean ... isNotificationRequired)
    {
        this.username = username;
        if (isNotificationRequired.length > 0 && isNotificationRequired[0]) notifyObservers();
    }

    public String getPassword()
    {
        return password;
    }

    public final void setPassword(String password, boolean ... isNotificationRequired)
    {
        this.password = password;
        if (isNotificationRequired.length > 0 && isNotificationRequired[0]) notifyObservers();
    }

}
