package com.warh.damlab3.adapter;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.warh.damlab3.R;
import com.warh.damlab3.dao.RecordatorioDataSource;
import com.warh.damlab3.dao.RecordatorioPreferencesDataSource;
import com.warh.damlab3.dao.RecordatorioRepository;
import com.warh.damlab3.model.RecordatorioModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.ViewHolder> {

    private List<RecordatorioModel> recordatorioDataSet;
    private static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        RecordatorioAdapter.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idInput;
        TextView fechaInput;
        TextView descripcionInput;
        Button borrarBtn;
        View view;

        public ViewHolder(View vistaRecordatorio){
            super(vistaRecordatorio);
            view = vistaRecordatorio;
            idInput = vistaRecordatorio.findViewById(R.id.VR_id_recordatorio);
            fechaInput = vistaRecordatorio.findViewById(R.id.VR_fecha_recordatorio);
            descripcionInput = vistaRecordatorio.findViewById(R.id.VR_descripcion_recordatorio);
            borrarBtn = (Button) vistaRecordatorio.findViewById(R.id.VR_boton_borrar);
            borrarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });

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

    public int getRecordatorioId(int position) {
        return recordatorioDataSet.get(position).getId();
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

        viewHolder.idInput.setText("#" + Integer.toString(recordatorioTemp.getId()));
        viewHolder.fechaInput.setText(formatter.format(recordatorioTemp.getFecha()));
        viewHolder.descripcionInput.setText(recordatorioTemp.getTexto());
    }

    @Override
    public int getItemCount() {
        return recordatorioDataSet.size();
    }
}
