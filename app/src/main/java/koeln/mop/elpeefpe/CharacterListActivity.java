package koeln.mop.elpeefpe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * An activity representing a list of Characters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CharacterDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CharacterListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CharacterCreateActivity.class);

                context.startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.character_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        DBHandler handler = new DBHandler(this);
        List<Character> list = handler.getAll();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(list));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Character> mValues;

        public SimpleItemRecyclerViewAdapter(List<Character> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            int woundLevel = (holder.mItem.elpe.damage.get(DamageType.VERZEHRT) - 1) / holder.mItem.elpe.value;
            holder.mContentView.setText(holder.mItem.name);
            if (woundLevel > 0) {
                holder.mWoundDeductionView.setText(String.valueOf((int) -Math.pow(2, woundLevel - 1)));
                holder.mWoundDeductionView.setTextColor(Color.RED);
            } else {
                holder.mWoundDeductionView.setText("0");
            }
            holder.mFocusLeftView.setText(String.valueOf(
                    holder.mItem.efpe.value - holder.mItem.efpe.sumDamage()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CharacterDetailActivity.class);
                intent.putExtra(CharacterDetailFragment.ARG_CHARACTER_ID, holder.mItem.id);

                context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView, mWoundDeductionView, mFocusLeftView;
            public Character mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                mWoundDeductionView = (TextView) view.findViewById(R.id.wound_deduction);
                mFocusLeftView = (TextView) view.findViewById(R.id.focus_left);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
