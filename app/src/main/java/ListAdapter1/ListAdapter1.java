package ListAdapter1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactbook.Contact;
import com.example.contactbook.CreateActivity;
import com.example.contactbook.MainActivity;
import com.example.contactbook.R;

import java.util.ArrayList;

import DBHelper.DBHelper;

public class ListAdapter1 extends RecyclerView.Adapter<ListAdapter1.ListHolder>
{
    MainActivity mainActivity;
    ArrayList<Contact> contactList;

    public ListAdapter1(MainActivity mainActivity, ArrayList<Contact> contactList) {
        this.mainActivity = mainActivity;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ListAdapter1.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.main_item,parent,false);
        ListHolder holder=new ListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter1.ListHolder holder, @SuppressLint("RecyclerView") int position) {
        Contact contact1=contactList.get(position);
        int id=contact1.getId();
        String name=contact1.getName();
        String number=contact1.getNumber();
        holder.Name.setText(""+name);
        holder.Number.setText(""+number);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mainActivity,holder.imageView);
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
                        else if(menuItem.getItemId()==R.id.delete)
                        {
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
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView Name,Number;
        ImageView imageView;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.main_name);
            Number = itemView.findViewById(R.id.main_number);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
