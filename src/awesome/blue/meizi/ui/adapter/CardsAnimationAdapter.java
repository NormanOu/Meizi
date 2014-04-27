
package awesome.blue.meizi.ui.adapter;

import android.R;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import awesome.blue.meizi.MeiziApplication;

import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;

public class CardsAnimationAdapter extends AnimationAdapter {

    private float mTranslationY = 150;

    private float mRotationX = 8;

    private long mDuration;

    public CardsAnimationAdapter(BaseAdapter baseAdapter) {
        super(baseAdapter);
        mDuration = MeiziApplication.getContext().getResources()
                .getInteger(R.integer.config_mediumAnimTime);
    }

    @Override
    protected long getAnimationDelayMillis() {
        return 30;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return mDuration;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationY", mTranslationY, 0),
                ObjectAnimator.ofFloat(view, "rotationX", mRotationX, 0)
        };
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void prepareAnimation(View view) {
        view.setTranslationY(mTranslationY);
        view.setRotationX(mRotationX);
    }
}
