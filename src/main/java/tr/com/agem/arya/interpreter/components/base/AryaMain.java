package tr.com.agem.arya.interpreter.components.base;

import android.widget.LinearLayout;

import tr.com.agem.arya.MainActivity;

public class AryaMain
{

    private AryaWindow aryaWindow;
    private AryaNavBar aryaNavBar;

    public AryaMain() {
    }

    public AryaMain(AryaWindow aryaWindow, AryaNavBar aryaNavBar) {
        this.aryaWindow = aryaWindow;
        this.aryaNavBar = aryaNavBar;
    }

    public AryaMain(MainActivity mainActivity, LinearLayout mainLayout) {
        this.aryaWindow = new AryaWindow(mainActivity,mainLayout);
        this.aryaNavBar = new AryaNavBar(mainActivity,mainLayout);
    }


    public AryaWindow getAryaWindow() {
        return aryaWindow;
    }

    public void setAryaWindow(AryaWindow aryaWindow) {
        this.aryaWindow = aryaWindow;
    }

    public AryaNavBar getAryaNavBar() {

        return aryaNavBar;
    }

    public void setAryaNavBar(AryaNavBar aryaNavBar) {

        this.aryaNavBar = aryaNavBar;
    }

}