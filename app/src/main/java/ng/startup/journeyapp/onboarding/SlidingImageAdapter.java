package ng.startup.journeyapp.onboarding;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ng.startup.journeyapp.R;


public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<Integer> images;
    private ArrayList<String> textStrings;
    private LayoutInflater inflater;
    private Context context;

    public SlidingImageAdapter(Context context, ArrayList<Integer> images, ArrayList<String> textStrings) {
        this.context = context;
        this.images=images;
        this.textStrings = textStrings;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.sliding_image_layout, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        TextView myText = (TextView) myImageLayout.findViewById(R.id.textView3);
        myImage.setImageResource(images.get(position));
        myText.setText(textStrings.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}