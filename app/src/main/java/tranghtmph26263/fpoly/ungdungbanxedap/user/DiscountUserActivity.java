package tranghtmph26263.fpoly.ungdungbanxedap.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import tranghtmph26263.fpoly.ungdungbanxedap.R;
import tranghtmph26263.fpoly.ungdungbanxedap.adapter.DiscountAdapter;
import tranghtmph26263.fpoly.ungdungbanxedap.adapter.DiscountUserAdapter;
import tranghtmph26263.fpoly.ungdungbanxedap.admin.DiscountActivity;
import tranghtmph26263.fpoly.ungdungbanxedap.dao.DiscountDAO;
import tranghtmph26263.fpoly.ungdungbanxedap.dao.DiscountUserDAO;
import tranghtmph26263.fpoly.ungdungbanxedap.dao.UserDAO;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.Discount;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.DiscountUser;
import tranghtmph26263.fpoly.ungdungbanxedap.entity.User;

public class DiscountUserActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DiscountUserAdapter adapter;
    ArrayList<Discount> arrayList;
    ArrayList<Discount> arrayListForUser = new ArrayList<Discount>();
    ArrayList<DiscountUser> arrayDiscountUser = new ArrayList<DiscountUser>();
    DiscountDAO dao;
    DiscountUserDAO discountUserDAO;
    String TAG = "aaaa";
    UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_user);

        userDAO = new UserDAO(this);
        SharedPreferences preferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String username = preferences.getString("USERNAME", "");
        User objUser = userDAO.selectOneWithUsername(username);

        recyclerView = findViewById(R.id.id_listDiscount);
        recyclerView.setHasFixedSize(true);

        dao = new DiscountDAO(this);
        discountUserDAO = new DiscountUserDAO(this);
        adapter = new DiscountUserAdapter(DiscountUserActivity.this, dao);

        arrayList =dao.selectAll();

        LocalDate today = LocalDate.now(); // ngay hom nay
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        for ( int i =0; i< arrayList.size(); i++){
            String endDate = arrayList.get(i).getExpiration_date();  // ngay het han
            LocalDate parsedDate = LocalDate.parse(endDate, formatter);

            int status;
            if (today.isBefore(parsedDate)) {
                Log.d(TAG, "so sanh date: con dung dc: "+ arrayList.get(i).getCode_name());
                Log.d(TAG, "today: "+ today+", ngay het han: "+ endDate);
                status = 1;

            } else if (today.isAfter(parsedDate)) {
                Log.d(TAG, "so sanh date: het han roi: "+ arrayList.get(i).getCode_name());
                Log.d(TAG, "today: "+ today+", ngay het han: "+ endDate);
                status = 0;
            } else {
                Log.d(TAG, "so sanh date: dung het hom nay: "+ arrayList.get(i).getCode_name());
                Log.d(TAG, "today: "+ today+", ngay het han: "+ endDate);
                status = 1;
            }

            if ( status == 1){
                arrayListForUser.add(arrayList.get(i));
            }
        }

        arrayDiscountUser = discountUserDAO.selectAll(objUser.getId());
        Log.d(TAG, "arrayDiscountUser: "+ arrayDiscountUser.toString());

        int i,j;
        for ( i=0; i<arrayDiscountUser.size(); i++){
            for ( j=0; j<arrayListForUser.size(); j++){
                if ( arrayDiscountUser.get(i).getDiscount_id() == arrayListForUser.get(j).getId() ){
                    arrayListForUser.remove(j);
                }
            }
        }

        if (arrayListForUser.isEmpty() ){
            Toast.makeText(this, "Chưa có phiếu giảm giá mới!", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Vui lòng chờ đợt giảm giá tiếp theo!", Toast.LENGTH_SHORT).show();
        }

        adapter.setData(arrayListForUser);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}