
package com.norika.android.library.base;

import android.os.Bundle;
import android.view.View;

import com.norika.android.library.R;

public abstract class SimpleFragment extends ProgressFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStatusBackground(R.drawable.i_bg_neterror);
    }

}
