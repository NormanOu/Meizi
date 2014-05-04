
package awesome.blue.meizi.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.data.RequestManager;
import awesome.blue.meizi.model.TopicM;
import awesome.blue.meizi.model.TopicM.TopicContentItem;

import com.android.volley.toolbox.ImageLoader;

public class TopicAdapter extends BaseAdapter {

    private List<TopicContentItem> mData;

    private LayoutInflater mLayoutInflater;

    private Drawable mLoadingImageDrawable;

    private Drawable mFailedImageDrawable;

    public TopicAdapter(Activity activity) {
        mLayoutInflater = activity.getLayoutInflater();
        mData = new ArrayList<TopicM.TopicContentItem>();

        mLoadingImageDrawable = activity.getResources().getDrawable(R.drawable.loading);
        mFailedImageDrawable = activity.getResources().getDrawable(R.drawable.loading_fail);
    }

    public void setData(List<TopicContentItem> data) {
        mData.clear();
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
            view = mLayoutInflater.inflate(R.layout.listitem_topic, null);
            holder = new Holder(view);
            view.setTag(holder);
        }

        if (holder.imageRequest != null) {
            holder.imageRequest.cancelRequest();
        }

        TopicContentItem item = mData.get(position);
        switch (item.type) {
            case IMAGE:
                holder.image.setVisibility(View.VISIBLE);
                holder.text.setVisibility(View.GONE);
                holder.imageRequest = RequestManager.loadImage(item.imgUrl, RequestManager
                        .getImageListener(holder.image, mLoadingImageDrawable,
                                mFailedImageDrawable, true));
                break;
            case MSG:
                holder.image.setVisibility(View.GONE);
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(item.msg);
                break;

            default:
                break;
        }

        return view;
    }

    private class Holder {
        public ImageView image;

        public TextView text;

        public View header;

        public ImageLoader.ImageContainer imageRequest;

        public Holder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            header = view.findViewById(R.id.header);
            header.setVisibility(View.GONE);
        }
    }
}
