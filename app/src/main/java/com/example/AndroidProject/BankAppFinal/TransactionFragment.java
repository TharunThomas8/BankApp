package com.example.AndroidProject.BankAppFinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.AndroidProject.BankAppFinal.Model.Transaction;
import com.example.AndroidProject.BankAppFinal.Adapters.TransactionAdapter;
import com.example.AndroidProject.BankAppFinal.Model.Profile;
import com.example.AndroidProject.bankscorpfinancial.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TransactionFragment extends Fragment {



    private TextView txtAccountName;
    private TextView txtAccountBalance;
    private TextView txtTransactionMsg;



    private ListView lstTransactions;

    private Profile userProfile;

    private int selectedAccountIndex;

    public TransactionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        getActivity().setTitle("Transactions");
        selectedAccountIndex = bundle.getInt("SelectedAccount", 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);

        txtAccountName = rootView.findViewById(R.id.txt_account_name);
        txtAccountBalance = rootView.findViewById(R.id.txt_account_balance);

        txtTransactionMsg = rootView.findViewById(R.id.txt_no_transactions);



        lstTransactions = rootView.findViewById(R.id.lst_transactions);

        ((DrawerActivity) getActivity()).showUpButton();

        setValues();
        return rootView;
    }


    private void setValues() {

        SharedPreferences userPreferences = getActivity().getSharedPreferences("LastProfileUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastProfileUsed", "");
        userProfile = gson.fromJson(json, Profile.class);

        setupTransactionAdapter(selectedAccountIndex);

        txtAccountName.setText("Account: " + userProfile.getAccounts().get(selectedAccountIndex).toTransactionString());
        txtAccountBalance.setText("Balance: Rs." + String.format(Locale.getDefault(), "%.2f",userProfile.getAccounts().get(selectedAccountIndex).getAccountBalance()));
    }


    private void setupTransactionAdapter(int selectedAccountIndex) {
        ArrayList<Transaction> transactions = userProfile.getAccounts().get(selectedAccountIndex).getTransactions();

        if (transactions.size() > 0) {

            txtTransactionMsg.setVisibility(GONE);
            lstTransactions.setVisibility(VISIBLE);
            TransactionAdapter transactionAdapter = new TransactionAdapter(getActivity(), R.layout.lst_transactions, transactions);
            lstTransactions.setAdapter(transactionAdapter);}
        else {
            txtTransactionMsg.setVisibility(VISIBLE);
            lstTransactions.setVisibility(GONE);
        }

    }
}
