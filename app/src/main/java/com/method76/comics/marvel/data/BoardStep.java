package com.method76.comics.marvel.data;

import android.content.Context;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.common.map.YutMap;
import com.method76.common.util.AndroidUtil;
import com.method76.common.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Sungjoon Kim on 2016-01-30.
 */
public class BoardStep implements AppConst {

    boolean opponent;
    int stepType, totalPower, x, y;   // 0:normal step, 1+:intersection no
    double angle, radiusRatio;
    LinkedList<BoardStep> shortcutRoute;
    HashMap<Integer, Runner> currRunners   = new HashMap<Integer, Runner>();
    HashMap<Integer, Runner> killedRunners = new HashMap<Integer, Runner>();

    public BoardStep(int type, LinkedList<BoardStep> next, double angle){
        this.stepType       = type;
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

    public BoardStep(int type, LinkedList<BoardStep> next, double angle,
                     double radiusRatio){
        this.stepType       = type;
        this.shortcutRoute  = next;
        this.angle          = angle;
        this.radiusRatio    = radiusRatio;
        if(radiusRatio!=0) {
            this.x = (int) (YutMap.MARGIN_LEFT +
                    (YutMap.RADIUS + YutMap.RADIUS * radiusRatio * Math.sin(angle)
                            + (Math.cos(angle) * YutMap.RADIUS * SHEAR_FACTOR))
            );
            this.y = (int) (YutMap.MARGIN_TOP + (YutMap.RADIUS
                    - YutMap.RADIUS * radiusRatio * Math.cos(angle)) * .79d);
        }else{
            this.x = YutMap.MARGIN_LEFT +YutMap.RADIUS;
            this.y = YutMap.MARGIN_TOP +YutMap.RADIUS;
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
        return this.x + AndroidUtil.dpToPixel(ctx, -5*currRunners.size());
    }

    public int getMultiY(Context ctx){
        return this.y + AndroidUtil.dpToPixel(ctx, 4*currRunners.size());
    }

    public int getWidth(){
        return (int)((3 - Math.cos(this.angle))/3*YutMap.CARD_WIDTH);
    }

    public int getHeight(){
        return (int)((3 - Math.cos(this.angle))/3*YutMap.CARD_HEIGHT);
    }

    public boolean isOccupied() {
        return currRunners.size()>0;
    }

    public LinkedList<BoardStep> getShortcutRoute() {
        return shortcutRoute;
    }

    public void removeRunners(){
        this.currRunners.clear();
    }

    /**
     * Todo: Remove and add
     * @param runner
     * @return
     */
    public MoveStatus addRunner(Runner runner) {
        this.opponent = runner.isOpponent();
        runner.setRunning(true);
        if(this.currRunners !=null && currRunners.size() > 0){
            // If some runner exists
            if(this.isOpponent() != runner.isOpponent()){
                // If the existing checker is Opponent's
                this.killedRunners = new HashMap<Integer, Runner>(this.currRunners);
                this.currRunners.clear();
                this.currRunners.put(runner.getId(), runner);
                return MoveStatus.KILL;
            }else{
                // If the existing checker is mine
                this.currRunners.put(runner.getId(), runner);
                return MoveStatus.JOIN;
            }
        }else{
            // If not any runner exists
            this.currRunners.put(runner.getId(), runner);
            return MoveStatus.NORMAL;
        }
    }

    public HashMap<Integer, Runner> getKilledRunners() {
        return killedRunners;
    }

    public boolean isOpponent() {
        return opponent;
    }

    public boolean isOpponent(boolean isMyTurn) {
        return opponent!=isMyTurn;
    }

}
