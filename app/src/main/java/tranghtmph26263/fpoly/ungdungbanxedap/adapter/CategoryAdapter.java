package tranghtmph26263.fpoly.ungdungbanxedap.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tranghtmph26263.fpoly.ungdungbanxedap.R;
import tranghtmph26263.fpoly.ungdungbanxedap.dao.CategoryDAO;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.Category;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.Product;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CategoryAdapter  extends  RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private ArrayList<Category> arrayList;
    CategoryDAO dao ;

    public CategoryAdapter(Context context, CategoryDAO dao) {
        this.context = context;
        this.dao = dao;
    }

    public void setData(ArrayList<Category> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_category, parent, false );
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        dao = new CategoryDAO(context);
        Category obj = arrayList.get(position);
        int index = position;

        if (obj == null) {
            return;
        }
        holder.tvName.setText(obj.getName());
        holder.tvid.setText(obj.getId()+ "");
        holder.tvActive.setText(obj.getActive()+"");

        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEdit(obj, index, context);
            }
        });

        holder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa thể loại?");
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setMessage("Có chắc chắn xóa: "+ obj.getName()+ "?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<Product> listSP = new ArrayList<Product>();
                        listSP.addAll(dao.checkProBeforeDelCategory(obj.getId()));
                        for ( int j =0; j<listSP.size(); j++){
                            Log.d("aaaa", "danh sach trc khi xoa: "+listSP.get(j).getName()+ "\t category id: "+listSP.get(j).getCategory_id() + "\t Stock: "+listSP.get(j).getStock());
                        }

                        int result = dao.softDelete(obj);
                        if ( result> 0){
                            arrayList.clear();
                            arrayList.addAll(dao.selectAll());
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();

                            for ( int j =0; j<listSP.size(); j++){
                                dao.updateStockPro(listSP.get(j));
                            }
                        }else{
                            Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void showDialogEdit( Category obj, int index, Context context){
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_edit_categoty);

        TextInputEditText ed_name = dialog.findViewById(R.id.ed_edit_nameCategory);
        ed_name.setText(obj.getName());

        Button btnSave = dialog.findViewById(R.id.btnSaveEditCategory);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ed_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập tên thể loại muốn thay đổi!", Toast.LENGTH_SHORT).show();
                    return;
                }
                obj.setName(ed_name.getText().toString().trim());
                int res = dao.updateRow(obj);
                if ( res > 0){
                    arrayList.set(index, obj);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Thay đổi thành công!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Thay đổi thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showDialogAdd( Context context){
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_add_category);

        TextInputEditText ed_name = dialog.findViewById(R.id.ed_add_nameCategory);
        Button btnSave = dialog.findViewById(R.id.btnSaveAddCategory);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( ed_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập tên thể loại muốn thêm!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Category obj = new Category();
                obj.setName(ed_name.getText().toString().trim());
                obj.setActive(1);
                long res = dao.insertNew(obj);
                if ( res > 0){
                    arrayList.clear();
                    arrayList.addAll(dao.selectAll());
                    notifyDataSetChanged();
                    Toast.makeText(context, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Thêm mới thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        if ( arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName, tvid, tvActive;
        private ImageView img_edit, img_del;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvActive = itemView.findViewById(R.id.id_active_category);
            tvName = itemView.findViewById(R.id.id_name_category);
            tvid = itemView.findViewById(R.id.id_category);
            img_edit = itemView.findViewById(R.id.edit_category);
            img_del = itemView.findViewById(R.id.del_category);
        }
    }
}
