package com.method76.comics.marvel.data;

import android.content.Context;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.common.map.YutMap;
import com.method76.common.util.AndroidUtil;
import com.method76.common.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Characteristics of each step on Yut-nori board
 * Created by Sungjoon Kim on 2016-01-30.
 */
public class BoardStep implements AppConst {

    boolean opponent;
    int x, y;
    double angle, radiusRatio;
    LinkedList<BoardStep> shortcutRoute;
    HashMap<Integer, Runner> occupyingRunners = new HashMap<Integer, Runner>();


    public BoardStep(LinkedList<BoardStep> next, double angle){
        this.shortcutRoute  = next;
        this.angle          = angle;
        this.radiusRatio    = 1d;
        this.x = (int)(YutMap.MARGIN_LEFT +
                (YutMap.RADIUS + YutMap.RADIUS * radiusRatio * Math.sin(angle)
                  + (Math.cos(angle) * YutMap.RADIUS * SHEAR_FACTOR))
            );
        this.y = (int)(YutMap.MARGIN_TOP + (YutMap.RADIUS
                - YutMap.RADIUS * radiusRatio * Math.cos(angle))*.79d);
    }

    public BoardStep(LinkedList<BoardStep> next, double angle, double radiusRatio){
        this.shortcutRoute  = next;
        this.angle          = angle;
        this.radiusRatio    = radiusRatio;
        if(radiusRatio!=0) {
            this.x = (int) (YutMap.MARGIN_LEFT +
                    (YutMap.RADIUS + YutMap.RADIUS * radiusRatio * Math.sin(angle)
                        + (Math.cos(angle) * YutMap.RADIUS * SHEAR_FACTOR)));
            this.y = (int) (YutMap.MARGIN_TOP + (YutMap.RADIUS
                    - YutMap.RADIUS * radiusRatio * Math.cos(angle)) * .79d);
        }else{
            this.x = YutMap.MARGIN_LEFT + YutMap.RADIUS;
            this.y = (int) (YutMap.MARGIN_TOP + (YutMap.RADIUS
                    - YutMap.RADIUS * 1.0f * Math.cos(ANGLE_90)) * .79d);
        }
    }


    public double getAngle() {
        return angle;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getMultiX(Context ctx){
        return this.x + AndroidUtil.dpToPixel(ctx, -5 * occupyingRunners.size());
    }

    public int getMultiY(Context ctx){
        return this.y + AndroidUtil.dpToPixel(ctx, 4 * occupyingRunners.size());
    }

    public int getWidth(){
        if(this.radiusRatio!=0) {
            // 일반적인 경우
            return (int) ((4 - Math.cos(this.angle)) / 5 * YutMap.CARD_WIDTH);
        }else{
            return (int) ((4 - Math.cos(ANGLE_90)) / 5 * YutMap.CARD_WIDTH);
        }
    }

    public int getHeight(){
        if(this.radiusRatio!=0) {
            // 일반적인 경우
            return (int) ((3 - Math.cos(this.angle)) /4 * YutMap.CARD_HEIGHT);
        }else{
            return (int) ((3 - Math.cos(ANGLE_90)) / 4 * YutMap.CARD_HEIGHT);
        }
    }

    public boolean isOccupied() {
        return occupyingRunners.size()>0;
    }

    public LinkedList<BoardStep> getShortcutRoute() {
        return shortcutRoute;
    }

    public boolean hasShortcut() {
        return (shortcutRoute!=null&&shortcutRoute.size()>0)?true:false;
    }

    public void removeAllRunners(){
        this.occupyingRunners.clear();
    }

    public void removeRunner(Runner runner){
        if(this.occupyingRunners.containsKey(runner.getId())){
            this.occupyingRunners.remove(runner.getId());
        }
    }

    public void addRunner(Runner runner){
        if(isOtherTeam(runner)){
            this.occupyingRunners.clear();
        }
        this.opponent = runner.isOpponent();
        this.occupyingRunners.put(runner.getId(), runner);
    }

    public HashMap<Integer, Runner> occupyingRunners() {
        return this.occupyingRunners;
    }

    public boolean isOtherTeam(Runner curRunner) {
        return opponent!=curRunner.isOpponent();
    }

}
