package fauzi.hilmy.quiznewsapple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fauzi.hilmy.quiznewsapple.api.ApiClient;
import fauzi.hilmy.quiznewsapple.api.ApiInterface;
import fauzi.hilmy.quiznewsapple.response.ArticlesItem;
import fauzi.hilmy.quiznewsapple.response.ResponseNews;
import fauzi.hilmy.quiznewsapple.response.Source;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadData();
    }

    private void loadData() {
        ApiInterface apiInterface = ApiClient.getInstance();
        Call<ResponseNews> call = apiInterface.readJadwalShalat();
        call.enqueue(new Callback<ResponseNews>() {
            @Override
            public void onResponse(Call<ResponseNews> call, Response<ResponseNews> response) {
                if (response.body().getStatus().equals("ok")) {
                    List<ArticlesItem> articlesItems = response.body().getArticles();
                    Log.e("Response", response.body().getStatus());

                    recycler.setHasFixedSize(true);
                    adapter = new CustomAdapter(articlesItems, MainActivity.this);
                    recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recycler.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ResponseNews> call, Throwable t) {
                Log.e("Error/Anzuyy", "", t);
            }
        });
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        List<ArticlesItem> articlesItems;
        Context context;

        public CustomAdapter(List<ArticlesItem> articlesItems, Context context) {
            this.articlesItems = articlesItems;
            this.context = context;
        }

        @NonNull
        @Override
        public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, final int position) {
            Source sources = (Source) articlesItems.get(position).getSource();
            holder.txtSource.setText(sources.getName());
            holder.txtTitle.setText(articlesItems.get(position).getTitle());
            holder.txtAuthor.setText(articlesItems.get(position).getAuthor());
//
//            String datee = articlesItems.get(position).getPublishedAt();
//            String newDate = datee.substring(1, 10);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date = dateFormat.parse(articlesItems.get(position).getPublishedAt());
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy");
                String date_release = newDateFormat.format(date);

                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                String date_hour = hourFormat.format(date);
                holder.txtDate.setText(date_release + " at " + date_hour);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Glide.with(context)
                    .load(articlesItems.get(position).getUrlToImage())
                    .centerCrop()
                    .into(holder.imgNews);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You Clicked "+ articlesItems.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return articlesItems.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imgNews;
            TextView txtDate, txtAuthor, txtSource, txtTitle;

            public MyViewHolder(View itemView) {
                super(itemView);
                imgNews = itemView.findViewById(R.id.imgBerita);
                txtDate = itemView.findViewById(R.id.txtDate);
                txtAuthor = itemView.findViewById(R.id.txtAuthor);
                txtSource = itemView.findViewById(R.id.txtSource);
                txtTitle = itemView.findViewById(R.id.txtTitle);
            }
        }
    }
}
