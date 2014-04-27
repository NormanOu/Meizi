
package awesome.blue.meizi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.data.RequestManager;
import awesome.blue.meizi.model.MeiziM;

import com.android.volley.toolbox.ImageLoader;

public class CategoryAdapter extends BaseAdapter {

    private List<MeiziM> mData;

    private LayoutInflater mLayoutInflater;

    private ListView mListView;

    private Drawable mDefaultImageDrawable = new ColorDrawable(Color.argb(255, 201, 201, 201));

    public CategoryAdapter(Activity activity, ListView listView) {
        mLayoutInflater = activity.getLayoutInflater();
        mData = new ArrayList<MeiziM>();
        mListView = listView;
    }

    public void addData(List<MeiziM> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view != null && view.getTag() != null) {
            holder = (Holder) convertView.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.listitem_category, null);
            holder = new Holder(view);
            view.setTag(holder);
        }

        if (holder.imageRequest != null) {
            holder.imageRequest.cancelRequest();
        }

        view
                .setEnabled(!mListView.isItemChecked(position + mListView.getHeaderViewsCount()));

        MeiziM meiziM = mData.get(position);
        holder.imageRequest = RequestManager
                .loadImage(meiziM.smallPicUrl, RequestManager
                        .getImageListener(holder.image, mDefaultImageDrawable,
                                mDefaultImageDrawable, true));

        return view;
    }

    private class Holder {
        public ImageView image;

        public ImageLoader.ImageContainer imageRequest;

        public Holder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
