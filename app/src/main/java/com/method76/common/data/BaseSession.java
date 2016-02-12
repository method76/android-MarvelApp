package com.method76.common.data;

/**
 * Created by Sungjoon Kim on 2016-02-10.
 */
public class BaseSession {

    private long lastAccessTime;
    private int interAdShowCount;
    private String accountName;
    private int accountType;
    private String accountEmail;
    private boolean isSessionExpired;

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getInterAdShowCount() {
        return interAdShowCount;
    }

    public void setInterAdShowCount(int interAdShowCount) {
        this.interAdShowCount = interAdShowCount;
    }

    public boolean isSessionExpired() {
        return isSessionExpired;
    }

    public void setIsSessionExpired(boolean isSessionExpired) {
        this.isSessionExpired = isSessionExpired;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
