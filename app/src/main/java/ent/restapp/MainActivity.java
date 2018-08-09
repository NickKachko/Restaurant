package ent.restapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private ArrayList<View> menuTables;
    // Key - dish name, value - pair of (price, count)
    private HashMap<String, Pair<Integer, Integer>> ordersMap;
    private Integer totalPrice = 0;
    private View imageView;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.main_layout);


        imageView = findViewById(R.id.item_photo);

        // Adding all tables to one container
        ordersMap = new HashMap<String, Pair<Integer, Integer>>();
        menuTables = new ArrayList<View>();
        menuTables.add(findViewById(R.id.main_menu_table));
        menuTables.add(findViewById(R.id.sushi_menu_table));
        menuTables.add(findViewById(R.id.pizza_menu_table));
        menuTables.add(findViewById(R.id.dessert_menu_table));
        menuTables.add(findViewById(R.id.drinks_menu_table));
        menuTables.add(findViewById(R.id.checkout_table));
        menuTables.add(findViewById(R.id.video_view));

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
//        hide();
//        main_menu_button
        findViewById(R.id.main_menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View main_menu_table = findViewById(R.id.main_menu_table);

                toggleView(main_menu_table);
            }
        });

        findViewById(R.id.sushi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View sushi_menu_table = findViewById(R.id.sushi_menu_table);

                toggleView(sushi_menu_table);
            }
        });

        findViewById(R.id.pizza_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View pizza_menu_table = findViewById(R.id.pizza_menu_table);

                toggleView(pizza_menu_table);
            }
        });

        findViewById(R.id.dessert_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dessert_menu_table = findViewById(R.id.dessert_menu_table);

                toggleView(dessert_menu_table);
            }
        });

        findViewById(R.id.drinks_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View drinks_menu_table = findViewById(R.id.drinks_menu_table);

                toggleView(drinks_menu_table);
            }
        });

        findViewById(R.id.video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View video_view = findViewById(R.id.video_view);
                VideoView v = (VideoView) video_view;
                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.vid);
                v.setVideoURI(uri);
                v.start();

                toggleView(video_view);
            }
        });






        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.INVISIBLE);
            }
        });


        // Main linkage of views to click listener is here
//        findViewById(R.id.main_menu_table).setOnClickListener(this);
//        findViewById(R.id.main_meals_row1).setOnClickListener(this);
//        findViewById(R.id.main_meals_row2).setOnClickListener(this);
//        findViewById(R.id.main_meals_row3).setOnClickListener(this);
//        findViewById(R.id.sushi_row1).setOnClickListener(this);
//        findViewById(R.id.sushi_row2).setOnClickListener(this);
//        findViewById(R.id.sushi_row3).setOnClickListener(this);

//        findViewById(R.id.main_meals_order1).setOnClickListener(this);
//        findViewById(R.id.main_meals_order2).setOnClickListener(this);
//        findViewById(R.id.main_meals_order3).setOnClickListener(this);
        findViewById(R.id.total_button).setOnClickListener(this);

        for (int i = 0; i < menuTables.size(); i++) {
            if (menuTables.get(i) instanceof TableLayout) {
                TableLayout in = (TableLayout) menuTables.get(i);
                for (int j = 1; j < in.getChildCount(); j++) {
                    in.getChildAt(j).setOnClickListener(this);
                    View b = ((TableRow) in.getChildAt(j)).getChildAt(1);
                    if (b instanceof Button) {
                        b.setOnClickListener(this);
                    }
                }
            }
        }






        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);



    }

    @Override
    public void onClick(View in) {
        View totalButton = findViewById(R.id.total_button);
        if (in instanceof Button) {
            if (in.getParent() instanceof TableRow) {
                handleOrderClick((Button) in);
            }
            else if (in == totalButton) {
                handleTotalButtonClick();
            }
        }
        if (in instanceof TableRow) {
            if (in.getParent() == findViewById(R.id.checkout_table)) {
                handleCheckoutTableRowClick((TableRow) in);
            } else {
                handleTableRowClick((TableRow) in);
            }
        }
        if (in instanceof TableLayout) {
//            totalButton.setTooltipText("Clicked on the table layout");
        }

    }

    private void handleOrderClick(Button in) {
            View tableRow = (TableRow) in.getParent();
            String dishName = ((TextView) ((TableRow) tableRow).getChildAt(0)).getText().toString();
            Integer dishPrice = 0;
            try {
                dishPrice = Integer.parseInt(((TextView) ((TableRow) tableRow).getChildAt(3)).getText().toString());
            } catch (Exception ex) {
                dishPrice = 120;
            }

            Integer dishCount = 1;

            if (dishPrice == 0) return;

            Pair<Integer, Integer> entry = ordersMap.get(dishName);

            if (entry != null) {
                dishCount = entry.second + 1;
            }

            ordersMap.put(dishName, Pair.create(dishPrice, dishCount));

            reCalculateTotalPrice();

            refreshCheckoutButton();
    }

    private void handleTableRowClick(TableRow in) {
        int selColor = getColor(R.color.menu_item_table_row_selected_bg_color);
        int unselColor = getColor(R.color.menu_item_table_row_unselected_bg_color);
        boolean shouldShow = false;

        Drawable background = in.getBackground();
        if (background == null) {
            shouldShow = true;
        } else if (background instanceof ColorDrawable) {
            if (((ColorDrawable) background).getColor() == selColor) {
                shouldShow = false;
            } else {
                shouldShow = true;
            }
        }

        View parentTable = (View) in.getParent();
        if (parentTable instanceof TableLayout) {
            hideAllTableRows((TableLayout) parentTable, unselColor);
        } else {
            return;
        }

        if (shouldShow) {
            View orderButton = in.getChildAt(1);
            if (orderButton instanceof Button) {
                orderButton.setVisibility(View.VISIBLE);
            }
            in.setBackgroundColor(selColor);
        }

    }

    private void handleCheckoutTableRowClick(TableRow in) {
        View firstElement = in.getChildAt(0);
        if (!(firstElement instanceof TextView)) {
            return;
        }

        String menuName = ((TextView)firstElement).getText().toString();
        ordersMap.remove(menuName);
        reCalculateTotalPrice();
        refreshCheckoutButton();
        clearCheckoutTable();
        if (totalPrice != 0) {
            populateCheckoutTable();
        } else {
            hideAllTables();
        }
    }

    private void handleTotalButtonClick() {
        hideAllTables();
        clearCheckoutTable();
        populateCheckoutTable();
    }

    private void clearCheckoutTable() {
        TableLayout checkoutTable = findViewById(R.id.checkout_table);
        int childCount = checkoutTable.getChildCount();

            checkoutTable.removeViews(1, childCount - 1);
    }

    private void populateCheckoutTable() {
        TableLayout checkoutTable = findViewById(R.id.checkout_table);
        TableRow rowToInsert;
        TextView textToInsert;

        for (Map.Entry<String, Pair<Integer, Integer>> entry : ordersMap.entrySet()) {
            rowToInsert = new TableRow(MainActivity.this);
//                    rowToInsert.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, (float) 0.5));

            textToInsert = new TextView(MainActivity.this);
            textToInsert.setText(entry.getKey().toString());
            textToInsert.setTextAppearance(R.style.MenuEntryText);
            rowToInsert.addView(textToInsert, new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, (float) 0.5));

            textToInsert = new TextView(MainActivity.this);
            textToInsert.setText(entry.getValue().first.toString());
            textToInsert.setTextAppearance(R.style.MenuEntryText);
            rowToInsert.addView(textToInsert, new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, (float) 0.2));
            textToInsert = new TextView(MainActivity.this);
            textToInsert.setText(entry.getValue().second.toString());
            textToInsert.setTextAppearance(R.style.MenuEntryText);
            rowToInsert.addView(textToInsert, new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, (float) 0.15));
            textToInsert = new TextView(MainActivity.this);
            textToInsert.setText(((Integer)(entry.getValue().first * entry.getValue().second)).toString());
            textToInsert.setTextAppearance(R.style.MenuEntryText);
            rowToInsert.addView(textToInsert, new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, (float) 0.15));

            rowToInsert.setVisibility(View.VISIBLE);
            rowToInsert.setOnClickListener(this);
            checkoutTable.addView(rowToInsert);
        }

        checkoutTable.setVisibility(View.VISIBLE);
    }

    private void reCalculateTotalPrice() {
        totalPrice = 0;
        for (Map.Entry<String, Pair<Integer, Integer>> entry : ordersMap.entrySet()) {
            totalPrice += entry.getValue().first * entry.getValue().second;
        }
    }

    private void refreshCheckoutButton() {
        Button checkoutButton = (Button) findViewById(R.id.total_button);
        if (totalPrice != 0) {
            checkoutButton.setText(getString(R.string.order_button) + ": " + totalPrice);
            checkoutButton.setVisibility(View.VISIBLE);
        } else {
            checkoutButton.setText(getString(R.string.order_button));
            checkoutButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggleView(View toEnable) {
        boolean shouldShow = (toEnable.getVisibility() == View.INVISIBLE);

        hideAllTables();

        if (shouldShow) {
            toEnable.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllTables() {
        for (int i = 0; i < menuTables.size(); i++) {
            menuTables.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void hideAllTableRows(TableLayout parent, int colorToFill) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TableRow child = (TableRow) parent.getChildAt(i);
            child.setBackgroundColor(colorToFill);

            View orderButton = child.getChildAt(1);
            if (orderButton instanceof Button) {
                orderButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
