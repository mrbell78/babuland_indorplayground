package com.example.babuland1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babuland1.R;
import com.example.babuland1.activity.QrcodeActivity;
import com.example.babuland1.activity.TicketfromListviewActivity;
import com.example.babuland1.model.Model_ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.Customlookclass> {


    Context context;
    List<Model_ticket> list;

    public TicketAdapter(Context context, List<Model_ticket> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Customlookclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_ticketview,parent,false);

        Customlookclass holder = new Customlookclass(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Customlookclass holder, final int position) {

      //  Model_ticket model_ticket=list.get(position);
      holder.tv_amount.setText(Integer.toString(list.get(position).getTotal()));


      holder.tv_status.setText(list.get(position).getStatus());
      holder.relativeListener.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, TicketfromListviewActivity.class);

              intent.putExtra("orderid",list.get(position).getOrderid());
              intent.putExtra("time",list.get(position).getTime());
              intent.putExtra("total",list.get(position).getTotal());
              intent.putExtra("status",list.get(position).getStatus());
              intent.putExtra("branch",list.get(position).getBranchname());
              context.startActivity(intent);
          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Customlookclass extends RecyclerView.ViewHolder {

         View mView;
         TextView tv_amount,tv_time,tv_status,tv_orderid;

         RelativeLayout relativeListener;
        public Customlookclass(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
            tv_amount=mView.findViewById(R.id.amountid);

            tv_status=mView.findViewById(R.id.statusid);

            relativeListener=mView.findViewById(R.id.relativeListener);
        }
    }
}
