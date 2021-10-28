package com.warh.damlab3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warh.damlab3.R;
import com.warh.damlab3.model.RecordatorioModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.ViewHolder> {

    private List<RecordatorioModel> recordatorioDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fechaInput;
        TextView descripcionInput;
        View view;

        public ViewHolder(View vistaRecordatorio){
            super(vistaRecordatorio);
            view = vistaRecordatorio;
            fechaInput = vistaRecordatorio.findViewById(R.id.VR_fecha_recordatorio);
            descripcionInput = vistaRecordatorio.findViewById(R.id.VR_descripcion_recordatorio);
        }

        public View contenedor(){
            return view;
        }

        public TextView getFechaInput(){
            return fechaInput;
        }

        public TextView getDescripcionInput(){
            return descripcionInput;
        }
    }

    public RecordatorioAdapter(List<RecordatorioModel> dataSet){
        recordatorioDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vista_recordatorio, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        RecordatorioModel recordatorioTemp = recordatorioDataSet.get(position);

        viewHolder.fechaInput.setText(formatter.format(recordatorioTemp.getFecha()));
        viewHolder.descripcionInput.setText(recordatorioTemp.getTexto());
    }

    @Override
    public int getItemCount() {
        return recordatorioDataSet.size();
    }
}
