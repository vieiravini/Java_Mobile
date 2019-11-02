package br.com.fatecararas.persistenciadedados.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fatecararas.persistenciadedados.R;
import br.com.fatecararas.persistenciadedados.model.Dicionario;

public class SQLiteAdapter extends BaseAdapter {
    private Context mContext;
    private List<Dicionario> dicionarioList;

    public SQLiteAdapter(Context context, List<Dicionario> dicionarioList) {
        this.mContext = context;
        this.dicionarioList = dicionarioList;
    }

    @Override
    public int getCount() {
        return dicionarioList.size();
    }

    @Override
    public Object getItem(int position) {
        return dicionarioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(dicionarioList.get(position).getId());
    }

    public View getView(final int pos, View child, final ViewGroup parent) {
        Holder mHolder;
        if (child == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.cell_layout, null);
            mHolder = new Holder();
            mHolder.textViewId = child.findViewById(R.id.textViewId);
            mHolder.textViewPalavra = child.findViewById(R.id.textViewPalavra);
            mHolder.textViewDefinicao = child.findViewById(R.id.textViewDefinicao);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }
        mHolder.textViewId.setText(String.valueOf(dicionarioList.get(pos).getId()));
        mHolder.textViewPalavra.setText(dicionarioList.get(pos).getPalavra());
        mHolder.textViewDefinicao.setText(dicionarioList.get(pos).getDefinicao());
        return child;
    }

    public class Holder {
        TextView textViewId;
        TextView textViewPalavra;
        TextView textViewDefinicao;
    }
}
