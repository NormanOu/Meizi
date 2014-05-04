
package awesome.blue.meizi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import awesome.blue.meizi.R;
import awesome.blue.meizi.ui.MainActivity;
import awesome.blue.meizi.ui.adapter.DrawerAdapter;

public class DrawerFragment extends Fragment {

    private ListView mListView;

    private DrawerAdapter mAdapter;

    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        View contentView = inflater.inflate(R.layout.fragment_drawer, null);

        mListView = (ListView) contentView.findViewById(R.id.listView);
        mAdapter = new DrawerAdapter(getActivity(), mListView);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.setItemChecked(position, true);
                mMainActivity.setCategory(mAdapter.getItem(position));
            }
        });

        return contentView;
    }
    // private

}
