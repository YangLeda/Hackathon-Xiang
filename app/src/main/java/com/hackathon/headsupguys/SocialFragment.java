package com.hackathon.headsupguys;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hackathon.headsupguys.MsgListAdapter;
import com.hackathon.headsupguys.OnMsgClickListener;
import com.hackathon.headsupguys.Message;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends Fragment {

    private List<Message> messageList;
    private RecyclerView mRecyclerView;
    private MsgListAdapter mMsgAdapter;
    private EditText mMsgInputText;
    private Button mMsgSbtBtn;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.msg_list);
        mMsgInputText = (EditText) view.findViewById(R.id.msg_input);
        mMsgSbtBtn = (Button) view.findViewById(R.id.msg_submit);

        messageList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        initInputText();

        loadMsgDataIntoList();

        setOnMsgClickListener();

        return view;
    }

    private void setOnMsgClickListener() {
        mMsgAdapter.setOnMsgClickListener(new OnMsgClickListener() {
            @Override
            public void onMsgClick(Message msg) {
                // TODO
                popupConfirmDialog(msg);
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMsgDataIntoList() {
        Message msg1 = new Message(1, "Jack", "I am so tired of working and I have to watch my phone!!!", "", "Unread");
        Message msg2 = new Message(2, "Jerry", "Studying at the moment, gonna leave my phone away.", "", "Unread");
        Message msg3 = new Message(3, "Caro", "Hi，我失恋了，真的很需要看手机了!!!", "", "Read");
        messageList.add(msg1);
        messageList.add(msg2);
        messageList.add(msg3);
        mMsgAdapter = new MsgListAdapter(messageList, getActivity());
        mRecyclerView.setAdapter(mMsgAdapter);
    }

    private void popupConfirmDialog(Message msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Make Your Choice");
        builder.setMessage("You are about to allow " + msg.getAuthor() + "'s request, please choose Accept or Decline?");
        builder.setCancelable(false);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "The request has been confirmed!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "The request has been declined!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private void initInputText() {

        mMsgSbtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = mMsgInputText.getText().toString();
                if(! TextUtils.isEmpty(txt)) {
                    Message msg = new Message(4, "Jason Chan", txt, "", "Read");
                    messageList.add(msg);
                    mMsgAdapter.notifyDataSetChanged();
                    hideKeyboard(getActivity().getApplicationContext(), mMsgInputText);
                    mMsgInputText.setText("");
                }
            }
        });
    }

    private void hideKeyboard(Context context, View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
