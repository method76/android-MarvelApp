package com.method76.comics.marvel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.common.map.YutMap;
import com.method76.comics.marvel.data.BoardStep;
import com.method76.comics.marvel.data.StepToMoveInfo;
import com.method76.comics.marvel.data.RolzResult;
import com.method76.comics.marvel.data.Runner;
import com.method76.comics.marvel.view.BoardRenderer;
import com.method76.common.base.BaseCompatActivity;
import com.method76.common.http.GsonRequest;
import com.method76.common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 네비드러워 템플릿 액티비티
 */
public class BoardActivity extends BaseCompatActivity implements AppConst,
        View.OnClickListener {

    @Bind(R.id.content_view)
    FrameLayout content_view;
    @Bind(R.id.surface_view)
    GLSurfaceView surface_view;
    @Bind(R.id.slot_1)
    ImageView card1;
    @Bind(R.id.slot_2)
    ImageView card2;
    @Bind(R.id.slot_3)
    ImageView card3;
    @Bind(R.id.slot_4)
    ImageView card4;
    @Bind(R.id.slot_11)
    ImageView card11;
    @Bind(R.id.slot_12)
    ImageView card12;
    @Bind(R.id.slot_13)
    ImageView card13;
    @Bind(R.id.slot_14)
    ImageView card14;
    @Bind(R.id.dice_result)
    ImageView dice_result;
    @Bind(R.id.img_dice)
    ImageView img_dice;
    @Bind(R.id.flame_circle)
    ImageView flame_circle;
    @Bind(R.id.run_advisory)
    TextView run_advisory;
    @Bind(R.id.run_count)
    LinearLayout run_count;
    @Bind(R.id.advisory_wrapper)
    LinearLayout advisory_wrapper;
    @Bind(R.id.checker_container_my)
    LinearLayout checker_container_my;
    @Bind(R.id.checker_container_opp)
    LinearLayout checker_container_opp;
    @Bind(R.id.esc_cnt_my)
    TextView esc_cnt_my;
    @Bind(R.id.esc_cnt_opp)
    TextView esc_cnt_opp;
    @Bind(R.id.txt_debug)
    TextView txt_debug;

    // 현재 움직여야 할 이동정보
    Button currStepToMove;

    boolean isMyTurn, isLastStepNum;
    int[] initSizeMy  = new int[2];
    int[] initSizeOpp = new int[2];
    int[] escapeCnt   = new int[2];
    LinkedList<BoardStep> yutRoute;
    ArrayList<ImageView> cardArrMy = new ArrayList<ImageView>();
    ArrayList<ImageView> cardArrOpp = new ArrayList<ImageView>();

    Context ctx;
    Handler lHandler = new DiceCallbackHandler();
    YutMap yMap;
    AlertDialog dialogQuitConfirm, dialogRetryConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        this.ctx = this;
        ButterKnife.bind(this);
        surface_view.setRenderer(new BoardRenderer(this));
        cardArrMy.add(card1);
        cardArrMy.add(card2);
        cardArrMy.add(card3);
        cardArrMy.add(card4);

        cardArrOpp.add(card11);
        cardArrOpp.add(card12);
        cardArrOpp.add(card13);
        cardArrOpp.add(card14);

        getParams();
        card1.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        card11.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        initSizeMy[0]  = card1.getMeasuredWidth();
        initSizeMy[1]  = card1.getMeasuredHeight();

        initSizeOpp[0] = card11.getMeasuredWidth();
        initSizeOpp[1] = card11.getMeasuredHeight();

        yMap = new YutMap(this, BOARD_DIAMETER_DP, initSizeMy[0], initSizeMy[1]);
        yutRoute = yMap.getMainRoute();

        Glide.with(this).load(R.drawable.bg_aura_dice).into(flame_circle);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Do you really want to quit Yut-nori?");
        ab.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BoardActivity.this.finish();
            }
        });
        ab.setNegativeButton(R.string.cancel, null);
        dialogQuitConfirm = ab.create();

        ab.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BoardActivity.this, CharChooseActivity.class);
                startActivity(intent);
                BoardActivity.this.finish();
            }
        });
        dialogRetryConfirm = ab.create();

        esc_cnt_my.setText(String.format(getString(R.string.esc_yours), 0));
        esc_cnt_opp.setText(String.format(getString(R.string.esc_opps), 0));

        initAndChangeTurn();
    }


    @OnClick(R.id.btn_quit)
    void showQuitConfirmDialog(){
        if(dialogQuitConfirm.isShowing()==false) {
            dialogQuitConfirm.show();
        }
    }


    /**
     * Get parameters came from previous page
     */
    private void getParams(){
        Bundle bundle = this.getIntent().getExtras();
        HashMap<Integer, String> myCharMap = null;
        HashMap<Integer, String> oppCharMap = null;
        if(bundle!=null) {
            myCharMap = (HashMap)bundle.getSerializable("myMap");
            oppCharMap = (HashMap)bundle.getSerializable("oppMap");
            Log.d("myCharMap: " + myCharMap.toString());
            Log.d("oppCharMap: " + oppCharMap.toString());
        }
        int i = 0;
        for (Map.Entry<Integer, String> et : myCharMap.entrySet()) {
            // Initialize my runner
            Runner runner = new Runner(et.getKey(), et.getValue(), false);
            runner.setViewId(cardArrMy.get(i).getId());
            Glide.with(this).load(THUMBS_URL + et.getKey() + ".jpg").into(cardArrMy.get(i));
            cardArrMy.get(i).setTag(runner);
            cardArrMy.get(i).setOnClickListener(this);
            i++;
        }
        i = 0;
        for (Map.Entry<Integer, String> et : oppCharMap.entrySet()) {
            // Initialize opp's runner
            ImageView runnerView = cardArrOpp.get(i);
            Runner runner = new Runner(et.getKey(), et.getValue(), true);
            runner.setViewId(runnerView.getId());
            Glide.with(this).load(THUMBS_URL + et.getKey() + ".jpg").into(runnerView);
            runnerView.setTag(runner);
            i++;
            // Defence code
            if(i>3){
                break;
            }
        }
    }


    class DiceCallbackHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_DICE_RESULT:
                    String resultStr = msg.getData().getString("resultStr");
                    final String idx = "" + resultStr.charAt(1);
                    dice_result.setImageResource(
                            getResources().getIdentifier("dice_" + idx, "drawable",
                                    getPackageName()));
                    dice_result.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handleDiceResult(Integer.parseInt(idx));
                        }
                    }, 1700);
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        // When you click runner
        disableMovableRunners();
        removeStep(currStepToMove);
        moveRunner((ImageView) v);
        //For test
//        testNewCharMove(charToMove);
    }


    /**
     * Roll the dice~
     */
    @OnClick(R.id.img_dice)
    void rollDice(){
        img_dice.setClickable(false);
        flame_circle.setVisibility(View.GONE);
        AnimationSet aSet = new AnimationSet(true);
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.throw_dice);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.drop_dice);
        aSet.addAnimation(anim1);
        aSet.addAnimation(anim2);
        img_dice.startAnimation(aSet);

        getBaseApplication().getRequestQueue().add(
                new GsonRequest<RolzResult>(
                        this.DICE_API_URL,
                        RolzResult.class,
                        null,
                        new Response.Listener<RolzResult>() {
                            @Override
                            public void onResponse(RolzResult result) {
                                Log.d("Dice result: " + result.getDetails());
                                String resultStr = result.getDetails().replace("(", "").replace(")", "")
                                        .replaceAll(" ", "").replaceAll("[+]", "");
                                Message msg = lHandler.obtainMessage();
                                msg.what = SHOW_DICE_RESULT;
                                Bundle data = new Bundle();
                                data.putString("resultStr", resultStr);
                                msg.setData(data);
                                lHandler.sendMessage(msg);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        if (e != null) {
                            Log.e(e);
                        }
                    }
                }));
    }


    /**
     * 주사위 던진 결과 강조하여 표시
     * @param
     */
    private void handleDiceResult(int stepsToMove) {

        dice_result.setVisibility(View.GONE);

        // 주사위 던진 회수 증가
        addStepNoButton(stepsToMove);

        // Organize num buttons layout
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.width       = dpToPixel(30);
        param.height      = dpToPixel(40);
        param.rightMargin = dpToPixel(3);

        // 주사위 결과 값에 따라 분기
        switch(stepsToMove){
            case YUT:
                // If Yut
                String msg = isMyTurn?"You made 'Yut', roll one more time!":"Opponent made 'Yut'!";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                if(isMyTurn==false){
                    // 상대방이 윷이면
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 주사위 던지기
                            rollDice();
                        }
                    }, OPPONENT_CONSIDER_INTERVAL);
                }else{
                    // 내가 윷시면
                    flame_circle.setVisibility(View.VISIBLE);
                    showMyTurn();
                }
                return;
            default:
                // If not Yut
                chooseMovableSteps();
                return;
        }

    }



    /**
     * Let the play choose step number to go
     */
    private void chooseMovableSteps(){

        int movableStepsCnt = run_count.getChildCount();
        if(movableStepsCnt==0){
            return;
        }

        int randIdx = 0;
        if (isMyTurn) {
            run_advisory.setText("Choose step number to move");
        }else{
            run_advisory.setText("Opponent is Considering...");
            randIdx = (int)(Math.random()*movableStepsCnt);
        }

        if(movableStepsCnt==1){
            // 1개 남으면 바로 선택되게 함
            currStepToMove = (Button)run_count.getChildAt(0);
            chooseMovableRunners(currStepToMove);
            return;
        }

        // 1개 이상이면: 선택할 수 있게 함
        for(int i=0;i<movableStepsCnt;i++){
            // Enlighten available steps
            Button btn = (Button)run_count.getChildAt(i);
            if(isMyTurn) {
                btn.setBackgroundResource(R.drawable.border_num_choose);
                btn.setClickable(true);
            }else{
                btn.setClickable(false);
                if(randIdx==i) {
                    opponentsStepClick(btn);
                    break;
                }
            }
        }
    }


    /**
     *
     * @param stepsToGo
     */
    private void addStepNoButton(int stepsToGo){
        // Add stepInfo to the Button
        StepToMoveInfo stepInfo = new StepToMoveInfo(stepsToGo);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = dpToPixel(30);
        params.height = dpToPixel(40);
        params.rightMargin = dpToPixel(3);
        Button btnStep = new Button(this);
        btnStep.setTag(stepInfo);
        btnStep.setLayoutParams(params);
        btnStep.setBackgroundResource(R.drawable.border_num_norm);
        btnStep.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
        btnStep.setText("" + stepsToGo);
        btnStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currStepToMove = (Button)v;
                removeStep(currStepToMove);
                disableMovableSteps();
                chooseMovableRunners(null);
            }
        });
        run_count.addView(btnStep);
        run_count.setVisibility(View.VISIBLE);
        currStepToMove = btnStep;

        // Refresh(Redraw) view container
        //      advisory_wrapper > run_count > stepCnt
        btnStep.requestLayout();
        run_count.requestLayout();
        advisory_wrapper.requestLayout();
    }

    private void clearSteps(){
        run_count.removeAllViews();
    }

    private void removeStep(Button num){
        currStepToMove = num;
        run_count.removeView(num);
    }

    private void opponentsStepClick(final Button btn){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn.performClick();
            }
        }, OPPONENT_CONSIDER_INTERVAL);
    }

    /**
     * Let the player choose a Runner to run
     * @param btnStep : If want remove btn ad param here
     */
    private void chooseMovableRunners(Button btnStep){
        int remainderCnt = 0;
        if(this.isMyTurn){
            // If my turn
            run_advisory.setText("Choose a runner to move");
            remainderCnt = cardArrMy.size();
            if(remainderCnt>1){
                // If remained runner is more than 1
                for (int i = 0; i < remainderCnt; i++) {
                    cardArrMy.get(i).setBackgroundResource(R.drawable.border_img_choose);
                    cardArrMy.get(i).setClickable(true);
                }
            }else if(remainderCnt==1){
                // If remained runner is 1
                cardArrMy.get(0).setBackgroundResource(R.drawable.border_img_choose);
                cardArrMy.get(0).setClickable(true);
                cardArrMy.get(0).performClick();
            }
        }else{
            // If opponent's turn
            run_advisory.setText("Opponent is Choosing a runner...");
            remainderCnt = cardArrOpp.size();
            final int idx = (remainderCnt>1)?(int)(Math.random()*remainderCnt):0;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeStep(currStepToMove);
                    moveRunner(cardArrOpp.get(idx));
                }
            }, 2000);
        }
    }


    private void disableMovableRunners(){
        if(this.isMyTurn){
            // If my turn
            run_advisory.setText("Choose a Runner");
            for(int i=0;i< cardArrMy.size();i++){
                cardArrMy.get(i).setBackgroundResource(R.drawable.border_img_my);
                cardArrMy.get(i).setClickable(false);
            }
        }
    }


    private void disableMovableSteps(){
        int movableStepsCnt = run_count.getChildCount();
        for(int i=0;i<movableStepsCnt;i++){
            final Button btn = (Button)run_count.getChildAt(i);
            btn.setBackgroundResource(R.drawable.border_num_norm);
            btn.setClickable(false);
        }
    }


    /**
     * Runner moving effect
     * @param runnerView
     */
    private void moveRunner(ImageView runnerView){

        Log.d("moveRunner");
        StringBuffer debugStr = new StringBuffer();

        final Runner runner = (Runner)runnerView.getTag();

        // NullPointer sometimes..
        if(runner==null){
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
            rollDice();
            return;
        }
        if(runner.isRunning()==false) {
            // 최초로 보드판에 들어가능 경우이면
            initializeRunner(runnerView);
        }

        int stepCntMove = ((StepToMoveInfo)currStepToMove.getTag()).getStepCnt();
        // 1-1) 현재 지점이 지름길인가?
        // 1-2) 목적지에 지름길이 있는 지 판단
        BoardStep currStep = runner.getCurrentBoardStep();
        currStep.removeRunner(runner);
        boolean hasShortcut = currStep.getShortcutRoute()!=null?true:false;
        LinkedList<BoardStep> routeToGo;
        int destIdx;

        if(hasShortcut){
            // 현재 점에 지름길이 있는 경우
            routeToGo = currStep.getShortcutRoute();
            runner.setCurrentRoute(routeToGo);
            destIdx = -1 + stepCntMove;
        }else{
            // 현재 점에 지름길이 없는 경우
            routeToGo = runner.getCurrentRoute();
            destIdx = runner.getPosition() + stepCntMove;
        }
        BoardStep destStep = null;
        if(destIdx < routeToGo.size()) {
            destStep = routeToGo.get(destIdx);
            // 목적지에 다른 러너가 있으면
            if(destStep.isOccupied()) {
                // 상대방이 점유 중이면
                if (destStep.isOpponent(runner.isOpponent())) {
                    // When killed enemy
                    HashMap<Integer, Runner> killed = destStep.toBeKilledRunners();
                    retreatKilledRunner(runnerView, killed, destStep);
                    // Roll once again
                    txt_debug.setText("occupy: " + destStep.isOccupied() + ", isOpponent: "
                            + destStep.isOpponent(runner.isOpponent()) + ", killed: " + killed.size());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isMyTurn){
                                showMyTurn();
                            }else{
                                showOpponentsTurn();
                            }
                        }
                    }, OPPONENT_CONSIDER_INTERVAL);
                    return;
                } else {
                    // When joined with teammates
                    String host = isMyTurn ? "Your" : "Opponent's";
                    Toast.makeText(this, host + " runner '" + runner.getName()
                            + "' joined with teammate!", Toast.LENGTH_SHORT).show();
                    txt_debug.setText("occupy: " + destStep.isOccupied() + ", isOpponent: "
                            + destStep.isOpponent(runner.isOpponent()));
                }

            }else{
                // 상대방이 점유 중이 아니면
                txt_debug.setText("occupy: false");
                if(destStep.getShortcutRoute()!=null){
                    // 지름길이 있으면
                    if(isMyTurn) {
                        Toast.makeText(this, "You can take a shortcut there!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            MoveStatus status = runnerMovingProcess(runnerView, destStep);
            runner.setPosition(destIdx);
        }else{
            // When escaped
            // If runner has escaped
            destStep = yMap.getMainRoute().get(0);
            // Escape!!
            runnerView.setVisibility(View.GONE);
            String prefix = "";
            if(isMyTurn){
                prefix = "Your";
                escapeCnt[0]++;
                esc_cnt_my.setText(String.format(getString(R.string.esc_yours), escapeCnt[0]));
                cardArrMy.remove(runnerView);
            }else{
                prefix = "Opponent's";
                escapeCnt[1]++;
                esc_cnt_opp.setText(String.format(getString(R.string.esc_opps), escapeCnt[1]));
                cardArrOpp.remove(runnerView);
            }
            if (escapeCnt[0] == 4 || escapeCnt[1] == 4) {
                // Game finished
                String msg = isMyTurn ? "You have won the game. Do you want to retry?"
                        : "Opponent has won the game. Do you want to retry?";
                dialogRetryConfirm.setMessage(msg);
                dialogRetryConfirm.show();
            }
            Toast.makeText(this, prefix + " runner '" + runner.getName()
                    + "' escaped from the prison", Toast.LENGTH_SHORT).show();
        }

//        FrameLayout.LayoutParams params =
//                (FrameLayout.LayoutParams)runnerView.getLayoutParams();

        int fromX = currStep.getX(); //yMap.getStartPoint()[0];
        int fromY = currStep.getY(); // yMap.getStartPoint()[1];
        Log.d("From x/y: " + fromX + ", " + fromY);
        Log.d("To x/y: " + destStep.getX() + ", " + destStep.getY());
        // Card move ani
        float destXDelta = destStep.getMultiX(this) - fromX;
        float destYDelta = destStep.getMultiY(this) - fromY;
        Log.d("Delta x/y: " + destXDelta + ", " + destYDelta);

//        Animation ani = new TranslateAnimation(fromX,
//                destXDelta, fromY, destYDelta);
//        ani.setFillAfter(true);
//        ani.setDuration(500);
        /*AnimationSet set = new AnimationSet(true);
        Animation animation1 = new TranslateAnimation(fromX,
                destXDelta, fromY, destYDelta);
        animation1.setFillAfter(true);
        set.addAnimation(animation1);
        set.setDuration(800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("onAnimationEnd");


            }
        });
        runnerView.startAnimation(set);*/
//        runnerView.startAnimation(ani);

        // If single or last of Yut
        if (run_count.getChildCount() == 0) {
            initAndChangeTurn();
        } else {
            chooseMovableSteps();
        }

    }


    /**
     *
     * @param runnerView
     * @param destStep
     */
    public MoveStatus runnerMovingProcess(ImageView runnerView, BoardStep destStep) {

        Runner runner = (Runner)runnerView.getTag();
        int stepCnt = ((StepToMoveInfo)currStepToMove.getTag()).getStepCnt();
        // 현재 경로
        BoardStep currStep = runner.getCurrentBoardStep();
        LinkedList<BoardStep> currRoute = runner.getCurrentRoute();
        // 다음 경로
        LinkedList<BoardStep> newRoute = currStep.getShortcutRoute();

        MoveStatus status = null;
        if(newRoute!=null){
            // If has shortcut route: Exception
            status = newRoute.get(stepCnt).moveRunner(currStep, runner);
        }else{
            // Else
            status = currRoute.get(stepCnt).moveRunner(currStep, runner);
        }

        // Real moving
        FrameLayout.LayoutParams param1 = (FrameLayout.LayoutParams)
                runnerView.getLayoutParams();
        param1.width      = destStep.getWidth();
        param1.height     = destStep.getHeight();
        param1.leftMargin = destStep.getMultiX(this);
        param1.topMargin  = destStep.getMultiY(this);
        runnerView.setLayoutParams(param1);

        return status;

    }


    private void initializeRunner(ImageView runnerView){
        Runner runner = (Runner)runnerView.getTag();
        if(runner.isRunning()==false) {
            // Remove from bench
            if (isMyTurn) {
                checker_container_my.removeView(runnerView);
            } else {
                checker_container_opp.removeView(runnerView);
            }
        }
        LinkedList<BoardStep> mainRoute = yMap.getMainRoute();
        runner.setCurrentRoute(mainRoute);

        // 러너 이미지에 스타일 주기
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.TOP | Gravity.LEFT;
        param.leftMargin = yMap.getStartPoint()[0];
        param.topMargin = yMap.getStartPoint()[1];
        Log.d("Start point: " + param.leftMargin + "/" + param.topMargin);
        runnerView.setLayoutParams(param);

        if(runner.isRunning()==false) {
            content_view.addView(runnerView);
        }

    }


    /**
     *
     */
    private void retreatKilledRunner(ImageView killerView, HashMap<Integer, Runner> killed,
                                     BoardStep destStep){

        Runner killer = (Runner)killerView.getTag();
        // 돌려보내고,
        for(Map.Entry<Integer, Runner> item : killed.entrySet()){
            Runner killedRunner = item.getValue();
            killedRunner.setRunning(false);
            killedRunner.setCurrentRoute(yMap.getMainRoute());
            killedRunner.setPosition(0);
            ImageView killedRunnerView = (ImageView)findViewById(killedRunner.getViewId());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
            content_view.removeView(killedRunnerView);
            Log.d("Retreating " + killedRunner.getName());
            if(killer.isOpponent()){
                // 상대방이면
                params.width  = initSizeMy[0];
                params.height = initSizeMy[1];
                killedRunnerView.setLayoutParams(params);
                checker_container_my.addView(killedRunnerView);
            }else{
                // If it's mine
                params.width  = initSizeOpp[0];
                params.height = initSizeOpp[1];
                killedRunnerView.setLayoutParams(params);
                checker_container_opp.addView(killedRunnerView);
            }
        }

        StringBuffer msg = new StringBuffer();
        msg.append(killed.size() > 1 ? "Multi kill] " : "");
        msg.append(killer.isOpponent() ? "Opponent's" : "Your");
        msg.append(" '" + killer.getName() + "' killed ");
        if (killer.isOpponent()) {
            msg.append("your ");
        } else {
            msg.append("opponent's ");
        }
        msg.append(killed.size() > 1 ? "runners!!!" : "runner!");

        int stepToMove = ((StepToMoveInfo)currStepToMove.getTag()).getStepCnt();
        if( stepToMove!=YUT){
            if(!killer.isOpponent()) {
                // If my turn
                msg.append("\nRoll the dice one more time!");
            }
            Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
        }

        MoveStatus status = runnerMovingProcess(killerView, destStep);
        killer.addPosition(stepToMove);
    }


    /**
     * Change turn of this Game
     */
    private void initAndChangeTurn(){

        clearSteps();
        disableAllRunners();

        if(this.isMyTurn){
            this.isMyTurn = false;
            showOpponentsTurn();
        }else{
            this.isMyTurn = true;
            showMyTurn();
        }
    }

    /**
     * 본인 차례임을 알림
     */
    private void showMyTurn(){
        img_dice.setClickable(true);
        img_dice.setVisibility(View.VISIBLE);
        flame_circle.setVisibility(View.VISIBLE);
        run_advisory.setTextColor(ContextCompat.getColor(
                this, R.color.font_color_order_summary));
//        if (currStepToMove!=null && ((StepToMoveInfo) currStepToMove.getTag()).getStepCnt() == YUT){
//            run_advisory.setText("Roll the dice\none more time!");
//        }else{
            run_advisory.setText("Your turn,\nRoll the dice");
//        }

    }


    /**
     * Show that opponent's turn now
     */
    private void showOpponentsTurn(){
        img_dice.setClickable(false);
        flame_circle.setVisibility(View.GONE);
        run_advisory.setTextColor(ContextCompat.getColor(this, R.color.font_color_splash_bottom));
        run_advisory.setText("Opponent's turn");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 주사위 던지기
                rollDice();
            }
        }, OPPONENT_CONSIDER_INTERVAL);

    }

    private void disableAllRunners(){
        for(int i=0;i< cardArrMy.size();i++){
            cardArrMy.get(i).setBackgroundResource(R.drawable.border_img_my);
            cardArrMy.get(i).setClickable(false);
        }
        for(int i=0;i < cardArrOpp.size();i++){
            cardArrOpp.get(i).setBackgroundResource(R.drawable.border_img_opp);
        }
    }


    @Override
    public void onBackPressed() {
        if(canEscape ==false) {
            canEscape = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canEscape = false;
                }
            }, 1500);
        }else{
            if(dialogQuitConfirm.isShowing()==false) {
                dialogQuitConfirm.show();
            }
        }
    }




    private void testNewCharMove(ImageView charToMove){

        LinkedList[] list = yMap.getAllRoutes();

        for(int j=0;j<list.length;j++) {

            for (int i = 0; i < list[j].size(); i++) {

                ImageView newCheck = new ImageView(this);
                BoardStep destStep = (BoardStep)list[j].get(i);

                Log.d("x/y: " + destStep.getX() + "/" + destStep.getY() + "("
                        + Math.toDegrees(destStep.getAngle()) + ")");

                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                param.width = destStep.getWidth();
                param.height = destStep.getHeight();
                param.gravity = Gravity.TOP | Gravity.LEFT;
                param.topMargin = destStep.getY();
                param.leftMargin = destStep.getX();
                newCheck.setScaleType(ImageView.ScaleType.CENTER_CROP);
                newCheck.setLayoutParams(param);
                if(destStep.getAngle()%(Math.PI/2)==0){
                    newCheck.setBackgroundResource(R.drawable.border_img_my);
                }
                if(destStep.getAngle()%Math.PI==0){
                    newCheck.setBackgroundResource(R.drawable.border_img_opp);
                }
                Glide.with(this).load(THUMBS_URL + ((String[])charToMove.getTag())[0] + ".jpg").into(newCheck);
                content_view.addView(newCheck);
            }
        }

    }


}
