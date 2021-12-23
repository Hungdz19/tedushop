package com.example.demosdk3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeverRoleAdapter extends ArrayAdapter<SeverRoleItem> {
    private List<SeverRoleItem> mArrayList = new ArrayList<SeverRoleItem>();

    @Override
    public void add(SeverRoleItem object) {
        mArrayList.add(object);
        super.add(object);
    }
    public SeverRoleAdapter(Activity a) {
        super(a, 0);
    }

    public int getCount() {
        return this.mArrayList.size();
    }

    public SeverRoleItem getItem(int index) {
        return this.mArrayList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.itemseverrole, parent, false);


        final TextView tv = (TextView) v.findViewById(R.id.tv);

        final SeverRoleItem item = getItem(position);
        tv.setText(item.name);

        return v;
    }
}
