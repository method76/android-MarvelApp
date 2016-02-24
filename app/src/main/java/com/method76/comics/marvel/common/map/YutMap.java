package com.method76.comics.marvel.common.map;

import android.content.Context;

import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.data.BoardStep;
import com.method76.common.util.AndroidUtil;
import com.method76.common.util.Log;

import java.util.LinkedList;

/**
 * Created by Sungjoon Kim on 2016-02-10.
 */
public class YutMap implements AppConst {

    public static int RADIUS;
    public static int MARGIN_TOP;
    public static int MARGIN_LEFT;
    public static int CARD_WIDTH;
    public static int CARD_HEIGHT;

    private BoardStep home;

    private LinkedList<BoardStep> borderRoute1 = new LinkedList<BoardStep>();
    private LinkedList<BoardStep> borderRoute2 = new LinkedList<BoardStep>();

    private LinkedList<BoardStep> borderRoute3 = new LinkedList<BoardStep>();
    private LinkedList<BoardStep> borderRoute4 = new LinkedList<BoardStep>();
    private LinkedList<BoardStep> borderRoute5 = new LinkedList<BoardStep>();

    public YutMap(Context context, int diameterDp){

        this.RADIUS      = AndroidUtil.dpToPixel(context, (int)(diameterDp*0.47f));
        this.MARGIN_TOP  = AndroidUtil.dpToPixel(context, 22);
        this.MARGIN_LEFT = AndroidUtil.dpToPixel(context, 37);
        this.CARD_WIDTH  = AndroidUtil.dpToPixel(context, 37.5f);
        this.CARD_HEIGHT = AndroidUtil.dpToPixel(context, 50f);

        // 홈
        home = new BoardStep(null, ANGLE_180);

        // 둘레길 4
        borderRoute2.add(new BoardStep(null, ANGLE_270));
        borderRoute2.add(new BoardStep(null, ANGLE_180 + EACH_STEP_ANGLE * 4));
        borderRoute2.add(new BoardStep(null, ANGLE_180 + EACH_STEP_ANGLE * 3));
        borderRoute2.add(new BoardStep(null, ANGLE_180 + EACH_STEP_ANGLE * 2));
        borderRoute2.add(new BoardStep(null, ANGLE_180 + EACH_STEP_ANGLE * 1));
        borderRoute2.add(home);

        // 지름길 5 (홈으로 오는 길)
        borderRoute5.add(new BoardStep(null, ANGLE_180, 1/3f));
        borderRoute5.add(new BoardStep(null, ANGLE_180, 2/3f));
        borderRoute5.add(home);
        // 중앙 포인트: 둘레길3-1, 3-2, 4가 join 하는 point
        BoardStep interCenter = new BoardStep(borderRoute5, ANGLE_0, 0);

        // 지름길 3-1 (첫번째 Intersection)
        borderRoute3.add(new BoardStep(null, ANGLE_90, 2/3f));
        borderRoute3.add(new BoardStep(null, ANGLE_90, 1/3f));
        borderRoute3.add(interCenter);

        // 지름길 3-2 (첫번째 Intersection 연장)
        borderRoute3.add(new BoardStep(null, ANGLE_270, 1/3f));
        borderRoute3.add(new BoardStep(null, ANGLE_270, 2/3f));
        borderRoute3.addAll(borderRoute2);
        // 첫번째 지름길 포인트
        BoardStep interRT = new BoardStep(borderRoute3, ANGLE_90);

        // 지름길 4 (두번째 Intersection)
        borderRoute4.add(new BoardStep(null, ANGLE_0, 2/3f));
        borderRoute4.add(new BoardStep(null, ANGLE_0, 1/3f));
        borderRoute4.add(interCenter);
        borderRoute4.addAll(borderRoute5);
        // 두번째 지름길 포인트
        BoardStep interLT = new BoardStep(borderRoute4, ANGLE_0);

        // 둘레길 1
        borderRoute1.add(home);
        borderRoute1.add(new BoardStep(null, ANGLE_180 - EACH_STEP_ANGLE * 1));
        borderRoute1.add(new BoardStep(null, ANGLE_180 - EACH_STEP_ANGLE * 2));
        borderRoute1.add(new BoardStep(null, ANGLE_180 - EACH_STEP_ANGLE * 3));
        borderRoute1.add(new BoardStep(null, ANGLE_180 - EACH_STEP_ANGLE * 4));
        borderRoute1.add(interRT);

        // 둘레길 2
        borderRoute1.add(new BoardStep(null, ANGLE_90 - EACH_STEP_ANGLE * 1));
        borderRoute1.add(new BoardStep(null, ANGLE_90 - EACH_STEP_ANGLE * 2));
        borderRoute1.add(new BoardStep(null, ANGLE_90 - EACH_STEP_ANGLE * 3));
        borderRoute1.add(new BoardStep(null, ANGLE_90 - EACH_STEP_ANGLE * 4));
        borderRoute1.add(interLT);

        // 둘레길 3
        borderRoute1.add(new BoardStep(null, - EACH_STEP_ANGLE * 1));
        borderRoute1.add(new BoardStep(null, - EACH_STEP_ANGLE * 2));
        borderRoute1.add(new BoardStep(null, - EACH_STEP_ANGLE * 3));
        borderRoute1.add(new BoardStep(null, - EACH_STEP_ANGLE * 4));
        // 둘레길 4 Join 포인트
        borderRoute1.addAll(borderRoute2);

        Log.w("Home x/y: " + home.getX() + "/" + home.getY());
    }

    public BoardStep getHome(){
        return home;
    }

    public LinkedList<BoardStep> getMainRoute(){
        return this.borderRoute1;
    }
    public LinkedList<BoardStep>[] getAllRoutes(){
        LinkedList[] ret = {this.borderRoute1, this.borderRoute3, this.borderRoute4};
        return ret;
    }

}
