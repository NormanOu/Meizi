
package awesome.blue.meizi.ui;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import awesome.blue.meizi.R;
import awesome.blue.meizi.model.Category;
import awesome.blue.meizi.model.Category.CategoryItem;
import awesome.blue.meizi.ui.fragment.CategoryFragment;
import awesome.blue.meizi.ui.fragment.DrawerFragment;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private CategoryItem mCategoryItem;

    private CategoryFragment mCategoryFragment;

    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(R.string.app_name);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open_desc, R.string.drawer_close_desc) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mMenu.findItem(R.id.action_refresh).setVisible(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mMenu.findItem(R.id.action_refresh).setVisible(true);
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setCategory(Category.ITEMS[0]);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.left_drawer, new DrawerFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setCategory(CategoryItem categoryItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (categoryItem == null || mCategoryItem == categoryItem) {
            return;
        }

        mCategoryItem = categoryItem;
        mCategoryFragment = new CategoryFragment(categoryItem);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mCategoryFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_refresh:
                mCategoryFragment.loadFirstPageAndScrollToTop();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
