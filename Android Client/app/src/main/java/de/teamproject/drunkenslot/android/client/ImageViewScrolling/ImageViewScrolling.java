package de.teamproject.drunkenslot.android.client.ImageViewScrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageViewScrolling extends FrameLayout
{
    private static int ANIMATION_DUR = 150;
    ImageView current_image,next_image;

    int last_result = 0;
    int old_value = 0;

    IEventEnd eventEnd;

    public void setEventEnd(IEventEnd eventEnd)
    {
        this.eventEnd = eventEnd;
    }

    public ImageViewScrolling(@NonNull Context context)
    {
        super(context);
        init(context);
    }

    public ImageViewScrolling(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater.from(context);//https://www.youtube.com/watch?v=Ja2MEpWUyYE
        //5:52
    }
}
