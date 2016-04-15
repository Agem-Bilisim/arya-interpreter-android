package tr.com.agem.arya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.concurrent.ExecutionException;

import tr.com.agem.arya.gateway.AryaInterpreterHelper;
import tr.com.agem.arya.gateway.WebServiceConnectionAsyncTask;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.script.ElementFunctions;
import tr.com.agem.core.gateway.model.AryaRequest;
import tr.com.agem.core.gateway.model.AryaResponse;
import tr.com.agem.core.gateway.model.RequestTypes;
import tr.com.agem.core.utils.AryaUtils;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    public static String inetAddr = "10.0.3.2"; /* AVD(emulator) = 10.0.2.2
                                                *  GENYMOTION = 10.0.3.2
                                                *  PHYSICAL DEVICE = make sure that your device and PC connected to the same network
                                                *  then type terminal ifconfig and paste the value "inet addr"
                                                */
    private static AlertDialog alertDialog;
    private LinearLayout mainLayout;
    private AryaMain main;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //for (Wrapped android.os.NetworkOnMainThreadException) exception about threads which use network
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.scrollViewContentLayout);
        setupScrollView((SwipeRefreshLayout) findViewById(R.id.swipe_view), (ScrollView) findViewById(R.id.scrollView));
        setupActionBar(getSupportActionBar());
        refresh(mainLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.clear();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (AryaUtils.isNotEmpty(main))
            if (AryaUtils.isNotEmpty(main.getAryaNavBar())) {
                menu = main.getAryaNavBar().fillMenuOptions(menu);
            }

        return true;
    }

    private void setupScrollView(final SwipeRefreshLayout mSwipeRefreshLayout, final ScrollView mScrollView){
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mScrollView.getScrollY() == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        refresh(mainLayout);    //TODO I just called refresh() function for simplicity, It should be implemented with more appropriate post function.
                    }
                }, 1000);
            }
        });
    }

    private void setupActionBar(ActionBar actionBar){
        actionBar.setTitle("arya");
        actionBar.hide();
    }
    public void refresh(View v) {

        // Prepare initial request
        AryaRequest request = new AryaRequest();
        // Menu
        AryaRequest requestMenu = new AryaRequest();
        requestMenu.setAction("menu");
        requestMenu.setRequestType(RequestTypes.VIEW_ONLY);
        WebServiceConnectionAsyncTask connThreadMenu = new WebServiceConnectionAsyncTask("http://" + inetAddr + ":8080/arya/rest/asya", requestMenu, getApplicationContext());
        String responseMenuStr = null;
        try {
            responseMenuStr = connThreadMenu.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (AryaInterpreterHelper.getjSessionId() == null || ElementFunctions.getLastPage() == null) {
            request.setAction("login");
            request.setRequestType(RequestTypes.VIEW_ONLY);

        } else {
            request.setAction(ElementFunctions.getLastPage());
            request.setRequestType(RequestTypes.valueOf(ElementFunctions.getReqType()));
        }


        WebServiceConnectionAsyncTask connThread = new WebServiceConnectionAsyncTask("http://" + inetAddr + ":8080/arya/rest/asya", request, getApplicationContext());

        String responseStr = null;
        try {
            responseStr = connThread.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (responseStr != null && responseMenuStr != null) {
            AryaResponse response = new AryaResponse();
            response.fromXMLString(responseStr);
            AryaResponse responseMenu = new AryaResponse();
            responseMenu.fromXMLString(responseMenuStr);

            main = new AryaMain(this, mainLayout);
            AryaInterpreterHelper.interpretResponseMenu(responseMenu, main);
            if (request.getAction().equals("login")) {
                AryaInterpreterHelper.interpretResponse(response, request.getAction(), true, main);
            } else {
                AryaInterpreterHelper.interpretResponse(response, request.getAction(), false, main);
            }

        } else {
            setupConnectionFailedDialog( new AlertDialog.Builder(MainActivity.this));

            // AlertController.setAndShowPrimerAlert(this, "HATA!", "Sunucuyla("+inetAddr+") Bağlantı Kurulamadı", "Tamam");}
        }
    }

    private void setupConnectionFailedDialog(AlertDialog.Builder alertDialog){
        alertDialog.setTitle("HATA");
        alertDialog.setMessage("Sunucuyla (" + inetAddr + ") baglanti kurulamadi. Adresi degistirmek icin asagidaki alanin kullaniniz.");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        alertDialog.setPositiveButton("Degistir",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        inetAddr = input.getText().toString();
                        refresh(mainLayout);
                    }
                });

        alertDialog.setNegativeButton("Degistirme",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        refresh(mainLayout);
                    }
                });


        alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);
        alertDialog.show();

    }
    public static AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public static void setAlertDialog(AlertDialog alertDialog) {

        MainActivity.alertDialog = alertDialog;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

}