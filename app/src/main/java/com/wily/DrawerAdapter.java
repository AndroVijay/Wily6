package com.wily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by neha on 05-04-2017.
 */

public class DrawerAdapter  extends ArrayAdapter<String> {

        ArrayList<String> list;
        int icons[] = {R.drawable.home_icon1,R.drawable.torch_icon,R.drawable.assistive_touch_menu,R.drawable.optoins_rate_us, R.drawable.options_share};
public DrawerAdapter(Context context, ArrayList<String> _list) {
        super(context, 0, _list);
        list = _list;
        }

//        R.drawable.options_screenshot,

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position);
        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        tvName.setText(name);
        icon.setBackgroundResource(icons[position]);
        return convertView;
        }
        }
