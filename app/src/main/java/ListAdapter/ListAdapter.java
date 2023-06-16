package ListAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.contactbook.Contact;
import com.example.contactbook.CreateActivity;
import com.example.contactbook.MainActivity;
import com.example.contactbook.R;

import java.util.ArrayList;

import DBHelper.DBHelper;

public class ListAdapter extends BaseAdapter
{
    MainActivity mainActivity;
    ArrayList<Contact> contactList;

    public ListAdapter(MainActivity mainActivity, ArrayList<Contact> contactList) {
        this.mainActivity = mainActivity;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(mainActivity).inflate(R.layout.main_item,parent,false);
        TextView Name = convertView.findViewById(R.id.main_name);
        TextView Number = convertView.findViewById(R.id.main_number);
        Contact contact1=contactList.get(position);
        int id=contact1.getId();
        String name=contact1.getName();
        String number=contact1.getNumber();
        Name.setText(""+name);
        Number.setText(""+number);

        ImageView imageView = convertView.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mainActivity,imageView);

                mainActivity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId()==R.id.update)
                        {
                            Intent intent = new Intent(mainActivity, CreateActivity.class);
                            intent.putExtra("id",id);
                            intent.putExtra("name",name);
                            intent.putExtra("number",number);
                            mainActivity.startActivity(intent);
                            mainActivity.finish();
                        }
                        else if(menuItem.getItemId()==R.id.delete){

                            DBHelper dbHelper = new DBHelper(mainActivity);

                            dbHelper.deleteContact(id);

                            contactList.remove(position);
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        return convertView;
    }
}
