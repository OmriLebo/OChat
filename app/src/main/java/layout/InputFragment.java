package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.omri.ochat.R;

public class InputFragment extends Fragment {

    private static final String BUTTON_TEXT = ">>"; //not in use for now
    private static final String EDITTEXT_HINT = "..."; // not in use for now
    private Button button;
    private EditText editText;


    private String buttonText;
    private String editTextHint;


    private OnInputListener mListener;

    public InputFragment() {
        // Required empty public constructor
    }

    public static InputFragment newInstance(String buttonText , String editTextHint) {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(BUTTON_TEXT, buttonText); // not in use for now
        args.putString(EDITTEXT_HINT, editTextHint); // not in use for now
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // creates the fragment and sets its texts... for now they are null cuz they are not in use
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            buttonText = getArguments().getString(BUTTON_TEXT);
            editTextHint = getArguments().getString(EDITTEXT_HINT);
            //button.setText(buttonText);
            //editText.setHint(editTextHint);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and sets a proper on click listner for button
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        button = (Button) view.findViewById(R.id.FragButton);
        editText = (EditText) view.findViewById(R.id.FragText);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    // calling the function from the activity class (context) that is using the fragment
                    mListener.onInput(editText.getText().toString());
                    editText.setText("");
                }
            }
        });

        if(buttonText != null)
            button.setText(buttonText);
        else
            button.setText(">>>");

        if(editTextHint != null)
            editText.setHint(editTextHint);
        else
            editText.setHint("...");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInputListener) {
            //defining witch activity class (context) is using the fragment
            mListener = (OnInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setFragTexts(String text , String hint){
        //to set the button text and EditText hint dynamicly (not from newInstance(..))
        if(text!=null)
            button.setText(text);
        if (hint != null)
            editText.setHint(hint);
    }

    public interface OnInputListener {
        // promising that the activity class (context) that uses the fragment will have this method
        // later on we call this function with mListener.onInput(text_from_editText) (context.function(args))
        void onInput(String text);
    }
}
