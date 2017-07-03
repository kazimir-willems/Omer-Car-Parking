package omer.parking.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import omer.parking.com.model.OfficeItem;
import omer.parking.com.ui.MainActivity;

import omer.parking.com.R;

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.OfficeViewHolder> {

    private MainActivity parent;
    private List<OfficeItem> items = new ArrayList<>();

    public OfficeAdapter(MainActivity parent) {
        this.parent = parent;
    }

    @Override
    public OfficeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_office, parent, false);
        return new OfficeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OfficeViewHolder holder, int position) {
        final OfficeItem item = items.get(position);

        holder.tvOfficeName.setText(item.getOfficeName());
        holder.tvOfficeAddress.setText(item.getOfficeAddress());

        holder.officeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.setOffice(item);
            }
        });
    }

    public OfficeItem getItem(int pos) {
        return items.get(pos);
    }

    public void clearItems() {
        items.clear();
    }

    public void addItem(OfficeItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<OfficeItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OfficeViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        @Bind(R.id.office_layout)
        LinearLayout officeLayout;
        @Bind(R.id.tv_office_name)
        TextView tvOfficeName;
        @Bind(R.id.tv_office_address)
        TextView tvOfficeAddress;

        public OfficeViewHolder(View view) {
            super(view);
            this.view = view;

            ButterKnife.bind(this, view);
        }
    }
}
