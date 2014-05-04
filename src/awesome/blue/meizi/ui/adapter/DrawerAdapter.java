
package awesome.blue.meizi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.model.Category;
import awesome.blue.meizi.model.Category.CategoryItem;

public class DrawerAdapter extends BaseAdapter {

    private Context mContext;

    private ListView mListView;

    public DrawerAdapter(Context context, ListView listView) {
        mContext = context;
        mListView = listView;
    }

    @Override
    public int getCount() {
        return Category.ITEMS.length;
    }

    @Override
    public CategoryItem getItem(int position) {
        if (position >= 0 && position < Category.ITEMS.length) {
            return Category.ITEMS[position];
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_drawer, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(getItem(position).getName());
        textView.setSelected(mListView.isItemChecked(position));
        return convertView;
    }
}
