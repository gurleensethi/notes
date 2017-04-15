package app.com.thetechnocafe.notes.Features.MainActivity;

import android.support.v4.app.Fragment;

/**
 * Created by gurleensethi on 13/04/17.
 */

public interface ActivityCallback {
    //Interface for fragments for callback
    void replaceFragment(Fragment fragment, boolean animate, boolean addToStack);

    void goBack();
}
