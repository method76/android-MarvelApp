package com.method76.comics.marvel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.method76.comics.marvel.adapter.ThumbnailAdapter;
import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.data.MarvelCharacters;
import com.method76.comics.marvel.data.substr.MarvelCharacter;
import com.method76.comics.marvel.view.ImageToast;
import com.method76.common.base.BaseApplication;
import com.method76.common.base.BaseCompatActivity;
import com.method76.common.constant.CommonConst;
import com.method76.common.http.GsonRequest;
import com.method76.common.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 네비드러워 템플릿 액티비티
 */
public class CharChooseActivity extends BaseCompatActivity implements CommonConst, AppConst {

    private Context context;
    private BaseApplication application;
    private HashMap<Integer, String> myChosenChars = new HashMap<Integer, String>();
    private HashMap<Integer, String> oppChosenChars = new HashMap<Integer, String>();
    private int selectedCnt = LIMIT_CHAR;
    GridLayoutManager mLayoutManager;
    List<MarvelCharacter> result;
    Handler uiHandler = new ThumbnailRecycleHandler();
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.bg_flame)
    ImageView bg_flame;
    @Bind(R.id.slot_1)
    ImageView slot_1;
    @Bind(R.id.slot_2)
    ImageView slot_2;
    @Bind(R.id.slot_3)
    ImageView slot_3;
    @Bind(R.id.slot_4)
    ImageView slot_4;

    ImageView[] cards = new ImageView[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = getCustomApplication();
        setContentView(R.layout.activity_char_choose);

        ButterKnife.bind(this);
        cards[0] = slot_1;
        cards[1] = slot_2;
        cards[2] = slot_3;
        cards[3] = slot_4;
        mLayoutManager = new GridLayoutManager(this, COL_CNT_GRID_ITEM);
        setBaseProgress(progress);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        setTitleStr(String.format(getString(R.string.select_checkers), selectedCnt));
        String url = GET_CHARACTERS + "?limit=" + GRID_LIST_CNT;
        Glide.with(this).load(R.drawable.bg_flame)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(bg_flame);

        getBaseApplication().getRequestQueue().add(new RestfulRequest(url).getGsonRequest());
    }


    /**
     * To get Marvel character info list
     */
    private class RestfulRequest {

        private String url;

        public RestfulRequest(String url){this.url = url;
            Log.d("Gson request to : " + url);}

        public GsonRequest<MarvelCharacters> getGsonRequest() {
            return new GsonRequest<MarvelCharacters>(
                    this.url,
                    MarvelCharacters.class,
                    null,
                    new Response.Listener<MarvelCharacters>() {
                        @Override
                        public void onResponse(MarvelCharacters result) {
                            List<MarvelCharacter> characters = result.getResults();
                            for(Iterator<MarvelCharacter> iter = characters.iterator(); iter.hasNext(); ){
                                MarvelCharacter item = iter.next();
                                if(item.getThumbnail().getPath().endsWith("image_not_available")){
                                    iter.remove();
                                    continue;
                                }
                            }
                            CharChooseActivity.this.result = characters;
                            Message msg = uiHandler.obtainMessage();
                            msg.what = UPDATE_LIST;
                            uiHandler.sendMessage(msg);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    if (e != null) {
                        Log.e(e);
                    }
                }
            });
        }
    }

    /**
     * Add Grid item to List view
     */
    private void updateList(){
        ThumbnailAdapter adapter = new ThumbnailAdapter(this, result);
        adapter.setHandler(uiHandler);
        recyclerView.setAdapter(adapter);
    }


    /**
     *
     */
    class ThumbnailRecycleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_LIST:
                    updateList();
                    break;
                case ITEM_SELECTED:
                    Log.d("msg: " + msg.arg1);
                    if(selectedCnt>0) {
                        if(myChosenChars.containsKey(msg.arg1)){
                            selectedCnt++;
                            myChosenChars.remove(msg.arg1);
                            displayCards();

                            if(selectedCnt<4){
                                setTitleStr(String.format(getString(R.string.select_more_checkers), selectedCnt));
                            }else{
                                setTitleStr(String.format(getString(R.string.select_checkers), selectedCnt));
                            }

                            break;
                        }else {
                            selectedCnt--;
                            myChosenChars.put(msg.arg1, msg.getData().getString(CHARACTER_INFO));
                            displayCards();
                            if (selectedCnt == 0) {
                                setTitleStr("Loading...");
                                launchGame();
                            }else {
                                if(selectedCnt<4){
                                    setTitleStr(String.format(getString(R.string.select_more_checkers), selectedCnt));
                                }else{
                                    setTitleStr(String.format(getString(R.string.select_checkers), selectedCnt));
                                }
                            }
                        }
                    }
                default:
                    break;
            }
        }
    }


    /**
     *
     */
    private void displayCards(){
        int idx = 3;
        for(Map.Entry<Integer, String> et : myChosenChars.entrySet()){
            Glide.with(this).load(THUMBS_URL + et.getKey() + ".jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(cards[idx]);
            idx--;
        }
        if(idx>-1){
            while(idx> 0) {
                Glide.with(this).load(R.drawable.ic_card_back)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(cards[idx]);
                idx--;
            }
        }
    }


    /**
     *
     */
    private void launchGame(){
        // Get opponents chars
        int cnt = 0;
        do{
            int randIdx = (int) (Math.random() * result.size());
            MarvelCharacter item = result.get(randIdx);
            Integer id = item.getId();
            String name = item.getName().indexOf("(")>-1
                    ?item.getName().substring(0, item.getName().indexOf("(")):item.getName();
            if(myChosenChars.containsKey(id)){
                continue;
            }else {
                oppChosenChars.put(id, name);
                cnt++;
            }
        }while(cnt<5);

        progress.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, BoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle extras = new Bundle();
                extras.putSerializable("myMap",  myChosenChars);
                extras.putSerializable("oppMap", oppChosenChars);
                intent.putExtras(extras);
                context.startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private void setTitleStr(String titleStr){
        bg_flame.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bg_flame.setVisibility(View.GONE);
            }
        }, FIRE_EFFECT_DURATION);
        title.setText(titleStr);
    }

    @Override
    public void onBackPressed() {
        if(canEscape ==false) {
            canEscape = true;
            ImageToast.makeText(this, "Press back again to quit",
                    Toast.LENGTH_SHORT, ImageToast.Status.NEUTRAL).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canEscape = false;
                }
            }, 1500);
        }else{
            super.onBackPressed();
        }
    }

}
