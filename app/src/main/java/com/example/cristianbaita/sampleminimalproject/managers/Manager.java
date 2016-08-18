package com.example.cristianbaita.sampleminimalproject.managers;

import android.util.Log;

/**
 * Created by cristian.baita on 3/14/2016.
 */
public class Manager {
    private final static String TAG = "Main Manager";
    private static Manager instance;

    private AnalyticsManager analyticsManager;
    private DbManager dbManager;
    private LoginManager loginManager;
    private NetworkManager networManager;
    private SoundManager soundManager;
    private VersionManager versionManager;

    private Manager(){
        analyticsManager = new AnalyticsManager();
        dbManager = new DbManager();
        loginManager = new LoginManager();
        networManager = new NetworkManager();
        soundManager = new SoundManager();
        versionManager = new VersionManager();
        Log.v(TAG, "Done initializing managers");
    }

    public static Manager getInstance() {
        if (instance == null) {
            instance = new Manager();
        }

        return instance;
    }



    public VersionManager getVersionManager() {
        return versionManager;
    }

    public void setVersionManager(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    public AnalyticsManager getAnalyticsManager() {
        return analyticsManager;
    }

    public void setAnalyticsManager(AnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public void setDbManager(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public NetworkManager getNetworManager() {
        return networManager;
    }

    public void setNetworManager(NetworkManager networManager) {
        this.networManager = networManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

}
