package utilities.phani.chitcalculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import utilities.phani.chitcalculator.model.Chit;
import utilities.phani.chitcalculator.service.ChitCalculator;
import utilities.phani.chitcalculator.service.FDCalculator;
import utilities.phani.chitcalculator.service.ProfitableBidNotFoundException;

public class ChitFragment extends Fragment {
    private final ChitCalculator chitCalculator = new ChitCalculator(new FDCalculator(), 5, 14);
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chit, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        calculateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chit chit = null;
                try {
                    chit = new Chit(chitValue(), totalMonths(), completedMonths());
                    bidText().setText(chitCalculator.bidSummary(chit, rateOfInterest()));
                } catch(ProfitableBidNotFoundException e) {
                    long commission = chitCalculator.commission(chit);
                    bidText().setText("No bid can fetch profit.\n"+chitCalculator.bidSummary(chit, rateOfInterest(), commission));
                } catch (RuntimeException e) {
                    bidText().setText("Invalid inputs.\n"+e.getMessage());
                }
            }
        });
        clearButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
k2     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private Button calculateButton() {
        return (Button)getActivity().findViewById(R.id.calculate);
    }

    private Button clearButton() {
        return (Button)getActivity().findViewById(R.id.clear);
    }

    private TextView bidText() {
        return (TextView) getActivity().findViewById(R.id.bid_text);
    }

    private double rateOfInterest() {
        return Double.parseDouble(roiField().getText().toString());
    }

    private EditText roiField() {
        return (EditText) getActivity().findViewById(R.id.roi);
    }

    private Spinner totalMonthsField() {
        return (Spinner)getActivity().findViewById(R.id.total_months);
    }

    private EditText chitValueField() {
        return (EditText) getActivity().findViewById(R.id.chit_value);
    }

    private EditText completedMonthsField() {
        return (EditText) getActivity().findViewById(R.id.completed_months);
    }

    private long chitValue() {
        return Long.parseLong(chitValueField().getText().toString());
    }

    private int totalMonths() {
        return Integer.parseInt(totalMonthsField().getSelectedItem().toString());
    }

    private int completedMonths() {
        return Integer.parseInt(completedMonthsField().getText().toString());
    }

    public void clear() {
        roiField().getText().clear();
        chitValueField().getText().clear();
        completedMonthsField().getText().clear();
        bidText().setText("");
    }
}
