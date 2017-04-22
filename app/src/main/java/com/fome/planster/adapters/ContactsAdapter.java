package com.fome.planster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.fome.planster.FontManager;
import com.fome.planster.models.Contact;
import com.fome.planster.R;
import com.fome.planster.UIManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by Alex on 03.04.2017.
 */
public class ContactsAdapter extends BaseAdapter {

    private com.fome.planster.models.Contacts contacts;
    private Context context;

    private ArrayList<Target> targets; // Strong refference to Picasso's bitmap receivers

    int wrapperStroke = 7; // selected contacts' stroke width in dp

    public ContactsAdapter (com.fome.planster.models.Contacts contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        targets = new ArrayList<>();
    }

    public void setContacts (com.fome.planster.models.Contacts contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.getSize();
    }

    @Override
    public Object getItem(int position) {
        return contacts.getContacts ().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactsAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.contact, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.check = (TextView) convertView.findViewById(R.id.contact_check);
            viewHolder.check.setTypeface(FontManager.getTypeface(context, FontManager.ICONFONT));
            viewHolder.wrapper = convertView.findViewById(R.id.contact_wrapper);
            viewHolder.contact = (Contact) getItem(position);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ContactsAdapter.ViewHolder) convertView.getTag();
        }

        Contact contact = viewHolder.contact;

        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.contact_thumbnail);
        Target target = new Target() { // Instantiating receiver
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                thumbnail.setImageBitmap(UIManager.getRoundedCornerBitmap(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}

        };
        Picasso.with(context).load(contact.getPhotoThumbnailUri()).into(target);
        targets.add(target); // setting strong reference to a receiver
        viewHolder.name.setText(contact.getName());

        boolean contactIsSelected = false;
        for (int i = 0; i < contacts.getSelectedContacts().size(); i++) { // check if contact is selected
            if (contacts.getSelectedContacts().get(i).getId() == contact.getId()) {
                contactIsSelected = true;
                break;
            }
        }

        if (contactIsSelected) {
            viewHolder.check.setVisibility(View.VISIBLE);
        } else {
            viewHolder.check.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void selectContact (int position, View view) {
        Contact contact = (Contact) getItem(position);
        ContactsAdapter.ViewHolder viewHolder = (ContactsAdapter.ViewHolder) view.getTag();

        if (contacts.getSelectedContacts().contains(contact)) {
            contacts.deselectContact(contact);
            viewHolder.check.setVisibility(View.GONE);
            viewHolder.isSelected = false;
            ViewPropertyObjectAnimator.animate(viewHolder.wrapper).width(viewHolder.wrapper.getWidth() - UIManager.dpToPx(context, wrapperStroke)).setDuration(300).start();
            ViewPropertyObjectAnimator.animate(viewHolder.wrapper).height(viewHolder.wrapper.getHeight() - UIManager.dpToPx(context, wrapperStroke)).setDuration(300).start();
        } else {
            contacts.selectContact(contact);
            viewHolder.check.setVisibility(View.VISIBLE);
            viewHolder.isSelected = true;
            ViewPropertyObjectAnimator.animate(viewHolder.wrapper).width(viewHolder.wrapper.getWidth() + UIManager.dpToPx(context, wrapperStroke)).setDuration(300).start();
            ViewPropertyObjectAnimator.animate(viewHolder.wrapper).height(viewHolder.wrapper.getHeight() + UIManager.dpToPx(context, wrapperStroke)).setDuration(300).start();
        }

        notifyDataSetChanged();
    }

    public class ViewHolder {
        Contact contact;
        TextView name;
        TextView check;
        View wrapper;
        boolean isSelected;
    }

}
