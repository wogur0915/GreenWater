package com.root.greenwater;

import static com.root.greenwater.WritePost.postNum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Plantlist extends Fragment {

    private View view;
    public FloatingActionButton addlist_btn;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private static int postNum;
    ListView listView;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.plantlist, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        listView = view.findViewById(R.id.memolist);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("greenwater");

        adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_list_item_1, arrayList);

        getPostNum();
        getPostingList();
        addlist_btn = view.findViewById(R.id.addlist_btn);
        addlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(WritePost.class);
            }
        });
        return view;
    }

    private void getPostingList() {
        for (int i = 1; i < postNum; i++) {
            String postN = String.format("post_%d", i);
            mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child(postN).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String title = snapshot.child("Title").getValue(String.class);
                    String contents = snapshot.child("Inner").getValue(String.class);
                    arrayList.add("※ "+title + "\n\n" + "  - "+ contents);
                    listView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void getPostNum() {
        mDatabaseRef.child("UserAccount").child(mFirebaseAuth.getUid()).child("PostingList").child("SavePostNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int getPNum = (int)dataSnapshot.getValue(Integer.class);
                postNum = getPNum;
                System.out.println("postNum : " + postNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
}
