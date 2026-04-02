package util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.button.MaterialButton;
import com.grocery.QTPmart.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class FabIconAnimator1 {

    private static final String ROTATION_Y_PROPERTY = "rotationY";

    private static final float TWITCH_END = 20F;
    private static final float TWITCH_START = 0F;
    private static final int DURATION = 200;

    @DrawableRes
    private int currentIcon;
    @StringRes
    private int currentText;
    private boolean isAnimating;

    //private final MaterialButton button;
    private final LinearLayout linearLayout;
    private final TextView txt_info;
    private final ConstraintLayout container;
    private final Transition.TransitionListener listener = new Transition.TransitionListener() {
        public void onTransitionStart(Transition transition) { isAnimating = true; }

        public void onTransitionEnd(Transition transition) { isAnimating = false; }

        public void onTransitionCancel(Transition transition) { isAnimating = false; }

        public void onTransitionPause(Transition transition) { }

        public void onTransitionResume(Transition transition) { }
    };

    public FabIconAnimator1(ConstraintLayout container) {
        this.container = container;
        //this.button = container.findViewById(R.id.fab);
        this.linearLayout = container.findViewById(R.id.img_info);
        this.txt_info = container.findViewById(R.id.txt_info);
    }

    public void update(@DrawableRes int icon, @StringRes int text) {
        boolean isSame = currentIcon == icon && currentText == text;
        currentIcon = icon;
        currentText = text;
        animateChange(icon, text, isSame);
    }

    public void setExtended(boolean extended) {
        setExtended(extended, false);
    }

    public void setOnClickListener(@Nullable View.OnClickListener clickListener) {
        if (clickListener == null) {
            //button.setOnClickListener(null);
            return;
        }
        AtomicBoolean flag = new AtomicBoolean(true);
        /*button.setOnClickListener(view -> {
            if (!flag.getAndSet(false)) return;
            clickListener.onClick(view);
            button.postDelayed(() -> flag.set(true), 2000);
        });*/
    }

    private boolean isExtended() { // R.dimen.triple_and_half_margin is 56 dp.
        //return button.getLayoutParams().height != button.getResources().getDimensionPixelSize(R.dimen.triple_and_half_margin);
        return linearLayout.getLayoutParams().height != linearLayout.getResources().getDimensionPixelSize(R.dimen.triple_and_half_margin_1);
    }

    private void animateChange(@DrawableRes int icon, @StringRes int text, boolean isSame) {
        boolean extended = isExtended();
        //button.setText(text);
        txt_info.setText(text);
        //button.setIconResource(icon);
        setExtended(extended, !isSame);
        if (!extended) twitch();
    }

    private void setExtended(boolean extended, boolean force) {
        if (isAnimating || (extended && isExtended() && !force)) return;

        ConstraintSet set = new ConstraintSet();
//        set.clone(container.getContext(), extended ? R.layout.fab_extended : R.layout.fab_collapsed);

        TransitionManager.beginDelayedTransition(container, new AutoTransition()
                .addListener(listener).setDuration(1500));


        if (extended) {
            //button.setText("detail");
            txt_info.setText("Detail ");
        }
        else{
            //button.setText("");
            txt_info.setText("");
        }

        set.applyTo(container);
    }

    private void twitch() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator twitchA = animateProperty(ROTATION_Y_PROPERTY, TWITCH_START, TWITCH_END);
        ObjectAnimator twitchB = animateProperty(ROTATION_Y_PROPERTY, TWITCH_END, TWITCH_START);

        set.play(twitchB).after(twitchA);
        set.start();
    }

    @NonNull
    private ObjectAnimator animateProperty(String property, float start, float end) {
        return ObjectAnimator.ofFloat(container, property, start, end).setDuration(DURATION);
    }
}
