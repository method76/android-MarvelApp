package com.method76.comics.marvel.common.constant;

/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public interface AppConst {
    // 3000calls/day, All endpoints currently accept only HTTP GET requests.
    // Ex) http://gateway.marvel.com/v1/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150
    // ts - a timestamp (or other long string which can change on a request-by-request basis)
    // hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
    // Data provided by Marvel. © 2014 Marvel link http://marvel.com
    String BASE_MARVEL_API              = "https://marvel-api-refinery-method76.c9users.io"; // or http
    String GET_CHARACTERS               = BASE_MARVEL_API + "/getrandhero";
    String THUMBS_URL                   = BASE_MARVEL_API + "/marvel_thumbs/";
    String DICE_API_URL                 = "https://rolz.org/api/?10d5.json";
    String CHARACTER_INFO               = "charInfo";

    int COL_CNT_GRID_ITEM               = 3;
    int UPDATE_LIST                     = 10;
    int ITEM_SELECTED                   = 11;
    int SHOW_DICE_RESULT                = 12;

    int LIMIT_CHAR                      = 4;
    int YUT                             = 4;

    int ALL_POINT_CNT                   = 20;
    int SIZE_S_CIRCLE                   = 26;
    int GRID_LIST_CNT                   = 30;
    int FIRE_EFFECT_DURATION            = 500;
    int BOARD_DIAMETER_DP               = 330;
    int OPPONENT_CONSIDER_INTERVAL      = 4000;

    double START_ANGLE_SPLASH           = Math.PI/ALL_POINT_CNT;
    double ANGLE_X_LEAN                 = Math.PI * 0.2d;
    double ANGLE_ADJUSTMENT             = 0; // - Math.PI * 1/30d;
    double ANGLE_0                      = ANGLE_ADJUSTMENT;
    double ANGLE_45                     = Math.PI * 1/4d + ANGLE_ADJUSTMENT;
    double ANGLE_90                     = Math.PI * 1/2d + ANGLE_ADJUSTMENT;
    double ANGLE_135                    = Math.PI * 3/4d + ANGLE_ADJUSTMENT;
    double ANGLE_180                    = Math.PI + ANGLE_ADJUSTMENT;
    double ANGLE_225                    = Math.PI * 5/4d + ANGLE_ADJUSTMENT;
    double ANGLE_270                    = Math.PI * 3/2d + ANGLE_ADJUSTMENT;
    double ANGLE_315                    = Math.PI * 7/4d + ANGLE_ADJUSTMENT;
    double EACH_STEP_ANGLE              = ANGLE_180 / ALL_POINT_CNT * 2d;

    float SHEAR_FACTOR                  = 0.33f;
    float ZOOM_FACTOR_BOARD             = 1.25f;

    enum MoveStatus{
        NORMAL,
        KILL,
        JOIN
    }
}
