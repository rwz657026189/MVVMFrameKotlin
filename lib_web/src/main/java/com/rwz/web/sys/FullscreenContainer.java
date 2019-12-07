package com.rwz.web.sys;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by jingbin on 2016/11/17.
 */

public class FullscreenContainer extends FrameLayout {

    public FullscreenContainer(Context ctx) {
        super(ctx);
        setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


}
