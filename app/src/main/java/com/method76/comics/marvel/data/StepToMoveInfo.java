package com.method76.comics.marvel.data;

import com.method76.comics.marvel.data.substr.MarvelCharacter;

import java.util.List;

/**
 * Created by Sungjoon Kim on 2016-01-30.
 */
public class StepToMoveInfo {

    private int stepCnt;
    private int index;

    public StepToMoveInfo(int stepCnt){
        this.stepCnt = stepCnt;
    }

//    public int getIndex() {
//        return index;
//    }
//
//    public void setIndex(int index) {
//        this.index = index;
//    }

    public int getStepCnt() {
        return stepCnt;
    }

    public void setStepCnt(int stepCnt) {
        this.stepCnt = stepCnt;
    }
}
