
package awesome.blue.meizi;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import awesome.blue.meizi.control.CategoryControl;
import awesome.blue.meizi.model.MeiziM;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Listener<List<MeiziM>> categoryListener = new Listener<List<MeiziM>>() {

            @Override
            public void onResponse(List<MeiziM> response) {
                // TODO Auto-generated method stub

            }
        };

        ErrorListener categoryErrorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        };
        CategoryControl.getAll(0, categoryListener, categoryErrorListener, MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
