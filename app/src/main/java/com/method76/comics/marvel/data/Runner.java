package com.method76.comics.marvel.data;

import android.widget.ImageView;

import com.method76.comics.marvel.common.constant.AppConst;

import java.util.LinkedList;

/**
 * Created by Sungjoon Kim on 2016-01-30.
 */
public class Runner implements AppConst {

    int position, power, id, joinDepth, viewId;
    boolean opponent, running;
    String name;
    LinkedList<BoardStep> currentRoute;


    public Runner(int id, String name, boolean isOpponent){
        this.id = id;
        this.name = name.trim();
        this.opponent = isOpponent;
    }


    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public LinkedList<BoardStep> getCurrentRoute() {
        return currentRoute;
    }

    public BoardStep getCurrentBoardStep() {
        return currentRoute.get(this.position);
    }

    public void setCurrentRoute(LinkedList<BoardStep> currentRoute) {
        this.currentRoute = currentRoute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return THUMBS_URL + this.id + ".jpg";
    }

    public boolean isOpponent() {
        return opponent;
    }

    public void setOpponent(boolean opponent) {
        this.opponent = opponent;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJoinDepth() {
        return joinDepth;
    }

    public void setJoinDepth(int joinDepth) {
        this.joinDepth = joinDepth;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

//    public int getX() {
//        return x;
//    }
//
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public void setY(int y) {
//        this.y = y;
//    }
}
