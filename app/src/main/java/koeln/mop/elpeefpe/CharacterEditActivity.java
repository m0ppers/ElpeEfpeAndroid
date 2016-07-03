package koeln.mop.elpeefpe;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Andreas Streichardt on 22.06.2016.
 */
public class CharacterEditActivity extends AppCompatActivity {
    private Character character;
    private CharacterForm characterForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);



        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            if (getIntent().hasExtra(CharacterDetailFragment.ARG_CHARACTER_ID)) {
                DBHandler dbHandler = new DBHandler(this);
                character = dbHandler.find(getIntent().getIntExtra(CharacterDetailFragment.ARG_CHARACTER_ID, 0));
            } else {
                character = new Character();
                character.name = "";
                character.setValues(0, 0);
            }

            CharacterEditFragment fragment = new CharacterEditFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.character_edit_container, fragment)
                    .commit();


            characterForm = new CharacterForm(character);
            fragment.setCharacterForm(characterForm);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, CharacterListActivity.class));
            return true;
        } else if (id == R.id.action_save) {
            DBHandler db = new DBHandler(this);

            if (characterForm.isValid()) {
                character.name = characterForm.getName().trim();
                character.elpe.value = Integer.parseInt(characterForm.getElpe());
                character.efpe.value = Integer.parseInt(characterForm.getEfpe());
                character = db.save(character);

                Intent intent = new Intent(this, CharacterDetailActivity.class);
                intent.putExtra(CharacterDetailFragment.ARG_CHARACTER_ID, character.id);

                this.startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }
}
