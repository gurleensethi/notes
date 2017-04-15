package app.com.thetechnocafe.notes;

import android.content.Context;

/**
 * Created by gurleensethi on 11/04/17.
 */

public interface BaseApp {
    interface View {
        Context getAppContext();
    }

    interface Presenter<T extends View> {
        void subscribe(T view);

        void unSubscribe();
    }
}
