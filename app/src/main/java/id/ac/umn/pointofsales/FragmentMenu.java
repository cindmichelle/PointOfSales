package id.ac.umn.pointofsales;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static android.content.ContentValues.TAG;


public class FragmentMenu extends Fragment{

    private CardView foodMenu;
    private CardView beverageMenu;
    private LinearLayout foodMenuBorder;
    private LinearLayout beverageMenuBorder;

    private FragmentListener listener;

    interface FragmentListener {
        void onButtonClicked(String text);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menus, container, false);

        foodMenu = view.findViewById(R.id.food_btn);
        beverageMenu= view.findViewById(R.id.beverage_btn);

        foodMenuBorder = view.findViewById(R.id.foodBorder);
        beverageMenuBorder = view.findViewById(R.id.beverageBorder);

        listener.onButtonClicked("food");
        foodMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodMenuBorder.setBackgroundResource(R.drawable.menu_clicked_border);
                beverageMenuBorder.setBackgroundResource(R.drawable.menu_border);
                listener.onButtonClicked("food");
            }
        });

        beverageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodMenuBorder.setBackgroundResource(R.drawable.menu_border);
                beverageMenuBorder.setBackgroundResource(R.drawable.menu_clicked_border);
                listener.onButtonClicked("beverage");
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentListener){
            listener = (FragmentListener) context;
        }
        else {
            throw new ClassCastException(context.toString());
        }
    }
}
