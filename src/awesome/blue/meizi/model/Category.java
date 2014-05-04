
package awesome.blue.meizi.model;

import awesome.blue.meizi.MeiziApplication;
import awesome.blue.meizi.R;

public class Category {

    public static final CategoryItem[] ITEMS = new Category.CategoryItem[] {
            new CategoryItem(R.string.drawer_item_all, 10),
            new CategoryItem(R.string.drawer_item_sexy, 1),
            new CategoryItem(R.string.drawer_item_boobs, 2),
            new CategoryItem(R.string.drawer_item_legs, 3),
            new CategoryItem(R.string.drawer_item_fresh, 11),
            new CategoryItem(R.string.drawer_item_hipsters, 12)
    };

    public static class CategoryItem {

        public CategoryItem(String name, int id) {
            mName = name;
            mID = id;
        }

        public CategoryItem(int nameResId, int id) {
            mName = MeiziApplication.getContext().getResources().getString(nameResId);
            mID = id;
        }

        public String getName() {
            return mName;
        }

        public int getID() {
            return mID;
        }

        private String mName;

        private int mID;
    }
}
