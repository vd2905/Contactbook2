package Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import model.Contact_model;
import com.example.contactbook.CreateActivity;
import com.example.contactbook.MainActivity;
import com.example.contactbook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import model.Database;

public class Recyclerview_Adapter extends RecyclerView.Adapter<Recyclerview_Adapter.ListHolder>
{
    MainActivity mainActivity;
    ArrayList<Contact_model> contactList;

    public Recyclerview_Adapter(MainActivity mainActivity, ArrayList<Contact_model> contactList) {
        this.mainActivity = mainActivity;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public Recyclerview_Adapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.main_item,parent,false);
        ListHolder holder=new ListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Recyclerview_Adapter.ListHolder holder, @SuppressLint("RecyclerView") int position) {
        Contact_model contact=contactList.get(position);
        int id=contact.getId();
        String name=contact.getName();
        String number=contact.getNumber();
        String imagePath=contact.getImagePath();
        holder.Name.setText(""+name);
        holder.Number.setText(""+number);
        loadImageFromStorage(contactList.get(position).getImagePath(),holder.imageView);

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mainActivity,holder.menu);
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
                            intent.putExtra("imagePath",imagePath);
                            mainActivity.startActivity(intent);
                            mainActivity.finish();
                        }
                        else if(menuItem.getItemId()==R.id.delete)
                        {
                            Database dbHelper = new Database(mainActivity);
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
        ImageView menu,imageView;
        public ListHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.main_name);
            Number = itemView.findViewById(R.id.main_number);
            menu = itemView.findViewById(R.id.menu);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
    private void loadImageFromStorage(String path,ImageView imageView)
    {

        try {
            File f=new File(path);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
