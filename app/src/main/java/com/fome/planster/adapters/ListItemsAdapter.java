package com.fome.planster.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fome.planster.FontManager;
import com.fome.planster.models.List;
import com.fome.planster.models.ListItem;
import com.fome.planster.R;

/**
 * Created by Alex on 30.03.2017.
 */
public class ListItemsAdapter extends BaseAdapter {

    List list;
    Context context;
    ListView listView;
    boolean editable;

    public ListItemsAdapter (Context context, List list, ListView listView, boolean editable) {
        this.list = list;
        this.context = context;
        this.listView = listView;
        this.editable = editable; // if list item's text can be changed
    }

    @Override
    public int getCount() {
        return list.getSize();
    }

    @Override
    public Object getItem(int position) {
        return list.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ListItem listItem = (ListItem) getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, null);

            FontManager.setFont(context, convertView, FontManager.BOLDFONT);

            EditText editText = (EditText) convertView.findViewById(R.id.list_item_input);
            viewHolder.editText = editText;
            viewHolder.removeButton = (LinearLayout) convertView.findViewById (R.id.remove_button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ((TextView) convertView.findViewById(R.id.list_item_num)).setText(String.valueOf(position + 1) + ".");

        viewHolder.ref = position;
        viewHolder.editText.setText(listItem.getText());

        ((TextView) viewHolder.removeButton.getChildAt(0)).setTypeface(FontManager.getTypeface(context, FontManager.ICONFONT));
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.removeItem(position);
                notifyDataSetChanged();
            }
        });
        if (editable) { // if text can be edited
            viewHolder.editText.setFocusable(true);
            viewHolder.editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void afterTextChanged(Editable arg0) {
                    list.setItemText(viewHolder.ref, arg0.toString());
                }
            });

            if (!list.getItem(list.getSize() - 1).getText().equals("")) {
                list.addItem(new ListItem());
                notifyDataSetChanged();
            }
            viewHolder.removeButton.setVisibility(View.GONE);
        } else {
            viewHolder.editText.setFocusable(false);
            viewHolder.removeButton.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        EditText editText;
        LinearLayout removeButton;
        int ref;
    }

}
